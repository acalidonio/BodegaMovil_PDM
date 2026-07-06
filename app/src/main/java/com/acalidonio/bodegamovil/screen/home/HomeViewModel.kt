package com.acalidonio.bodegamovil.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acalidonio.bodegamovil.di.AppContainer
import com.acalidonio.bodegamovil.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val scannedSku: String? = null,
    val scanError: String? = null,
    val isVerifyingScan: Boolean = false
)

class HomeViewModel(
    private val inventoryRepository: InventoryRepository = AppContainer.inventoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onBarcodeScanned(barcode: String?) {
        if (!barcode.isNullOrBlank()) {
            viewModelScope.launch {
                _uiState.update { it.copy(isVerifyingScan = true, scanError = null) }
                val product = inventoryRepository.validateAndFetchProduct(barcode)
                
                if (product != null) {
                    _uiState.update { it.copy(scannedSku = barcode, isVerifyingScan = false) }
                } else {
                    _uiState.update { it.copy(scanError = "Producto no encontrado en el sistema", isVerifyingScan = false) }
                }
            }
        } else {
            _uiState.update { it.copy(scanError = "Código escaneado no válido o vacío") }
        }
    }

    fun onScanError(errorMessage: String?) {
        _uiState.update { it.copy(scanError = errorMessage ?: "Error desconocido al escanear") }
    }

    fun onNavigatedToDetail() {
        _uiState.update { it.copy(scannedSku = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(scanError = null) }
    }
}
