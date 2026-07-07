package com.acalidonio.bodegamovil.screen.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acalidonio.bodegamovil.model.ProductCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    skuToEdit: String?,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: ProductFormViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.reset()
    }

    LaunchedEffect(skuToEdit) {
        if (skuToEdit != null) {
            viewModel.loadProductForEdit(skuToEdit)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.onSaveSuccessHandled()
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (skuToEdit != null) "Editar Producto" else "Nuevo Producto",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(innerPadding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text("Campos marcados con '*' son obligatorios.", modifier = Modifier.padding(bottom = 8.dp), color = Color.Red)

                OutlinedTextField(
                    value = uiState.sku,
                    onValueChange = { viewModel.updateField("sku", it) },
                    label = { Text("SKU *") },
                    enabled = skuToEdit == null,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.updateField("name", it) },
                    label = { Text("Nombre del Producto *") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = uiState.stock,
                    onValueChange = { viewModel.updateField("stock", it) },
                    label = { Text("Stock Actual *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = uiState.location,
                    onValueChange = { viewModel.updateField("location", it) },
                    label = { Text("Ubicación Física") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = uiState.imageUrl,
                    onValueChange = { viewModel.updateField("imageUrl", it) },
                    label = { Text("Enlace de Imagen") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ).fillMaxWidth(),
                        readOnly = true,
                        value = uiState.category?.displayName ?: "Seleccionar categoría",
                        onValueChange = {},
                        label = { Text("Categoría *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        ProductCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.displayName) },
                                onClick = {
                                    viewModel.updateCategory(category)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Text("Especificaciones Técnicas (Opcional)", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

                OutlinedTextField(
                    value = uiState.material,
                    onValueChange = { viewModel.updateField("material", it) },
                    label = { Text("Material") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = uiState.innerDiameter,
                    onValueChange = { viewModel.updateField("innerDiameter", it) },
                    label = { Text("Diámetro Interno") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = uiState.outerDiameter,
                    onValueChange = { viewModel.updateField("outerDiameter", it) },
                    label = { Text("Diámetro Externo") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = uiState.width,
                    onValueChange = { viewModel.updateField("width", it) },
                    label = { Text("Ancho") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = uiState.weight,
                    onValueChange = { viewModel.updateField("weight", it) },
                    label = { Text("Peso") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )

                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = { viewModel.saveProduct() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = !uiState.isSaving
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(if (skuToEdit != null) "Guardar Cambios" else "Crear Producto")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
