package com.acalidonio.bodegamovil.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.acalidonio.bodegamovil.di.AppContainer

data class DetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = true,
    val isUploading: Boolean = false,
    val error: String? = null,
    val isActionSuccess: Boolean = false
)

class DetailViewModel(
    private val repository: InventoryRepository = AppContainer.inventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadProduct(sku: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getProductBySku(sku).collect { product ->
                _uiState.update { 
                    it.copy(
                        product = product,
                        isLoading = false
                    ) 
                }
            }
        }
    }

    fun updateProduct(sku: String, product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, error = null, isActionSuccess = false) }
            try {
                repository.updateProduct(sku, product)
                _uiState.update { it.copy(isUploading = false, isActionSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isUploading = false, error = e.message ?: "Error al actualizar") }
            }
        }
    }

    fun deleteProduct(sku: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, error = null, isActionSuccess = false) }
            try {
                repository.deleteProduct(sku)
                _uiState.update { it.copy(isUploading = false, isActionSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isUploading = false, error = e.message ?: "Error al eliminar") }
            }
        }
    }
    
    fun resetActionState() {
        _uiState.update { it.copy(error = null, isActionSuccess = false) }
    }
}
