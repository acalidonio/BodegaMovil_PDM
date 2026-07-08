package com.acalidonio.bodegamovil.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acalidonio.bodegamovil.data.remote.ServerApiException
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.acalidonio.bodegamovil.di.AppContainer

import com.acalidonio.bodegamovil.repository.TokenRepository
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class DetailUiState(
    val product: Product? = null,
    val formattedAuditDate: String = "",
    val isLoading: Boolean = true,
    val isUploading: Boolean = false,
    val error: String? = null,
    val isActionSuccess: Boolean = false,
    val isAdmin: Boolean = false
)

class DetailViewModel(
    private val repository: InventoryRepository = AppContainer.inventoryRepository,
    private val tokenRepository: TokenRepository = AppContainer.tokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            tokenRepository.getUserDetails().collect { user ->
                if (user != null) {
                    _uiState.update { it.copy(isAdmin = user.role == "ADMIN") }
                }
            }
        }
    }

    fun loadProduct(sku: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isActionSuccess = false, product = null) }
            repository.getProductBySku(sku).collect { product ->
                _uiState.update { 
                    it.copy(
                        product = product,
                        formattedAuditDate = formatAuditDate(product?.lastAudit),
                        isLoading = false
                    ) 
                }
            }
        }
    }

    private fun formatAuditDate(isoDate: String?): String {
        if (isoDate == null) return "N/A"
        return try {
            val instant = Instant.parse(isoDate)
            val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-ES"))
            localDate.format(formatter)
        } catch (_: Exception) {
            isoDate
        }
    }

    fun deleteProduct(sku: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, error = null, isActionSuccess = false) }
            try {
                repository.deleteProduct(sku)
                _uiState.update { it.copy(isUploading = false, isActionSuccess = true) }
            } catch (e: ServerApiException) {
                _uiState.update { it.copy(isUploading = false, error = e.serverMessage) }
            } catch (_: Exception) {
                _uiState.update { it.copy(isUploading = false, error = "No se pudo conectar al servidor. Verifica tu conexión a internet.") }
            }
        }
    }
    
    fun resetActionState() {
        _uiState.update { it.copy(error = null, isActionSuccess = false) }
    }
}
