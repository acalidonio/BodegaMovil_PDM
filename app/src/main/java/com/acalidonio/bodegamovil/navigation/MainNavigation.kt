package com.acalidonio.bodegamovil.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.acalidonio.bodegamovil.screen.detail.ProductDetailScreen
import com.acalidonio.bodegamovil.screen.form.ProductFormScreen
import com.acalidonio.bodegamovil.screen.home.HomeScreen
import com.acalidonio.bodegamovil.screen.login.LoginScreen
import com.acalidonio.bodegamovil.screen.login.LoginViewModel

@Composable
fun BodegaMovilApp(
    viewModel: LoginViewModel = viewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    when (isLoggedIn) {
        null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        false -> {
            LoginScreen(
                onLoginSuccess = {}
            )
        }
        true -> {
            MainNavigation(onLogout = { viewModel.logout() })
        }
    }
}

@Composable
fun MainNavigation(onLogout: () -> Unit) {
    val backStackState = remember { mutableStateOf<List<Routes>>(listOf(Routes.Home)) }

    val provider = remember {
        entryProvider {
            entry<Routes.Home> {
                HomeScreen(
                    onNavigateToProductDetail = { sku -> backStackState.value += Routes.ProductDetail(sku) },
                    onNavigateToCreateProduct = { backStackState.value += Routes.ProductForm(null) },
                    onLogout = onLogout
                )
            }
            entry<Routes.ProductDetail> { route ->
                ProductDetailScreen(
                    sku = route.sku,
                    onBackClick = { backStackState.value = backStackState.value.dropLast(1) },
                    onNavigateToEdit = { backStackState.value += Routes.ProductForm(it) }
                )
            }
            entry<Routes.ProductForm> { route ->
                ProductFormScreen(
                    skuToEdit = route.sku,
                    onBackClick = { backStackState.value = backStackState.value.dropLast(1) },
                    onSaveSuccess = { backStackState.value = backStackState.value.dropLast(1) }
                )
            }
        }
    }

    NavDisplay(
        backStack = backStackState.value,
        onBack = { if (backStackState.value.size > 1) backStackState.value = backStackState.value.dropLast(1) },
        entryProvider = provider,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(500)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(500)
            )
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(250)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(250)
            )
        }
    )
}


