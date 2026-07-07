package com.acalidonio.bodegamovil.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.runtime.remember
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.acalidonio.bodegamovil.di.AppContainer
import com.acalidonio.bodegamovil.screen.dashboard.DashboardScreen
import com.acalidonio.bodegamovil.screen.profile.ProfileScreen
import com.acalidonio.bodegamovil.screen.search.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProductDetail: (String) -> Unit,
    onNavigateToCreateProduct: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) } // 0: Dashboard, 1: Search, 2: Profile
    var globalSearchQuery by rememberSaveable { mutableStateOf("") }
    
    val userDetails by AppContainer.tokenRepository.getUserDetails().collectAsStateWithLifecycle(initialValue = null)
    val isAdmin = userDetails?.role == "ADMIN"
    
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val scannerOptions = remember {
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .enableAutoZoom()
            .build()
    }
    val scanner = remember { GmsBarcodeScanning.getClient(context, scannerOptions) }

    LaunchedEffect(uiState.scannedSku) {
        uiState.scannedSku?.let { sku ->
            onNavigateToProductDetail(sku)
            viewModel.onNavigatedToDetail()
        }
    }

    LaunchedEffect(uiState.scanError) {
        uiState.scanError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (selectedTab == 0 || selectedTab == 1) {
                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = globalSearchQuery,
                            onValueChange = { 
                                globalSearchQuery = it
                                if (it.isNotEmpty() && selectedTab == 0) {
                                    selectedTab = 1
                                }
                            },
                            placeholder = { Text("Buscar productos, SKU...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = Color.DarkGray,
                                unfocusedBorderColor = Color.DarkGray
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Column {
                if (isAdmin) {
                    FloatingActionButton(
                        onClick = onNavigateToCreateProduct,
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo Producto")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                FloatingActionButton(
                    onClick = { 
                        scanner.startScan()
                            .addOnSuccessListener { barcode ->
                                viewModel.onBarcodeScanned(barcode.rawValue)
                            }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Escanear QR"
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inicio") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedTab == 1) Icons.Filled.Search else Icons.Outlined.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    label = { Text("Buscar") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedTab == 2) Icons.Filled.Home else Icons.Outlined.Person,
                            contentDescription = "Perfil"
                        )
                    },
                    label = { Text("Perfil") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Crossfade(
                targetState = selectedTab,
                label = "Home Navigation Crossfade",
                modifier = Modifier.fillMaxSize()
            ) { tab ->
                when (tab) {
                    0 -> DashboardScreen()
                    1 -> SearchScreen(
                        initialQuery = globalSearchQuery,
                        onProductClick = onNavigateToProductDetail
                    )
                    2 -> ProfileScreen(onLogout = onLogout)
                }
            }
            
            if (uiState.isVerifyingScan) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
