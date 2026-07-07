package com.acalidonio.bodegamovil.screen.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.model.ProductCategory
import com.acalidonio.bodegamovil.model.StockStatus
import com.acalidonio.bodegamovil.repository.InventoryRepository
import com.acalidonio.bodegamovil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductFormUiState(
    val isEditMode: Boolean = false,
    val sku: String = "",
    val name: String = "",
    val location: String = "",
    val stock: String = "",
    val innerDiameter: String = "",
    val outerDiameter: String = "",
    val width: String = "",
    val weight: String = "",
    val material: String = "",
    val imageUrl: String = "",
    val category: ProductCategory? = null,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class ProductFormViewModel(
    private val repository: InventoryRepository = AppContainer.inventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductFormUiState())
    val uiState: StateFlow<ProductFormUiState> = _uiState.asStateFlow()
    
    private var originalSku: String? = null

    fun reset() {
        originalSku = null
        _uiState.value = ProductFormUiState()
    }

    fun loadProductForEdit(sku: String) {
        originalSku = sku
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isEditMode = true) }
            repository.getProductBySku(sku).collect { product ->
                if (product != null) {
                    _uiState.update {
                        it.copy(
                            sku = product.sku,
                            name = product.name,
                            location = product.location,
                            stock = product.stock.toString(),
                            innerDiameter = product.innerDiameter ?: "",
                            outerDiameter = product.outerDiameter ?: "",
                            width = product.width ?: "",
                            weight = product.weight ?: "",
                            material = product.material ?: "",
                            imageUrl = product.imageUrl ?: "",
                            category = product.category,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun updateField(field: String, value: String) {
        _uiState.update { state ->
            when (field) {
                "sku" -> state.copy(sku = value)
                "name" -> state.copy(name = value)
                "location" -> state.copy(location = value)
                "stock" -> state.copy(stock = value)
                "innerDiameter" -> state.copy(innerDiameter = value)
                "outerDiameter" -> state.copy(outerDiameter = value)
                "width" -> state.copy(width = value)
                "weight" -> state.copy(weight = value)
                "material" -> state.copy(material = value)
                "imageUrl" -> state.copy(imageUrl = value)
                else -> state
            }
        }
    }

    fun updateCategory(category: ProductCategory?) {
        _uiState.update { it.copy(category = category) }
    }

    fun saveProduct() {
        val state = _uiState.value
        if (state.name.isBlank() || state.sku.isBlank() || state.stock.isBlank() || state.category == null) {
            _uiState.update { it.copy(error = "Nombre, SKU, Stock y Categoría son obligatorios") }
            return
        }

        val stockInt = state.stock.toIntOrNull()
        if (stockInt == null || stockInt < 0) {
            _uiState.update { it.copy(error = "El stock debe ser un número válido") }
            return
        }
        
        val status = when {
            stockInt == 0 -> StockStatus.OUT_OF_STOCK
            stockInt < 10 -> StockStatus.LOW_STOCK
            else -> StockStatus.AVAILABLE
        }

        val product = Product(
            sku = state.sku,
            name = state.name,
            location = state.location.ifBlank { "N/A" },
            stock = stockInt,
            status = status,
            lastAudit = java.time.LocalDate.now().toString(),
            innerDiameter = state.innerDiameter.ifBlank { null },
            outerDiameter = state.outerDiameter.ifBlank { null },
            width = state.width.ifBlank { null },
            weight = state.weight.ifBlank { null },
            material = state.material.ifBlank { null },
            imageUrl = state.imageUrl.ifBlank { null },
            category = state.category
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                if (state.isEditMode && originalSku != null) {
                    repository.updateProduct(originalSku!!, product)
                } else {
                    repository.createProduct(product)
                }
                _uiState.update { it.copy(isSaving = false, isSuccess = true) }
            } catch (_: Exception) {
                _uiState.update { it.copy(isSaving = false, error = "No se pudo conectar al servidor. Verifica tu conexión a internet.") }
            }
        }
    }

    fun onSaveSuccessHandled() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
