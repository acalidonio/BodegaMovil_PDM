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
    val isLoading: Boolean = true
)

class DetailViewModel(
    private val repository: InventoryRepository = AppContainer.inventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadProduct(sku: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val product = repository.getProductBySku(sku)
            _uiState.update { 
                it.copy(
                    product = product,
                    isLoading = false
                ) 
            }
        }
    }
}
