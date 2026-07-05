package com.acalidonio.bodegamovil.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acalidonio.bodegamovil.di.AppContainer
import com.acalidonio.bodegamovil.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.acalidonio.bodegamovil.data.remote.ApiClient
import com.acalidonio.bodegamovil.repository.TokenRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class LoginViewModel(
    private val userRepository: UserRepository = AppContainer.userRepository,
    private val tokenRepository: TokenRepository = AppContainer.tokenRepository
) : ViewModel() {
    val isLoggedIn: StateFlow<Boolean?> = tokenRepository.getToken()
        .map { token -> 
            if (token != null) {
                ApiClient.authToken = token
                true
            } else {
                false
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(employeeId: String, pass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val success = userRepository.login(employeeId, pass)
            if (success) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Credenciales incorrectas o error de red") }
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            tokenRepository.clearToken()
            ApiClient.authToken = null
            resetState()
        }
    }
    
    fun resetState() {
        _uiState.update { LoginUiState() }
    }
}
