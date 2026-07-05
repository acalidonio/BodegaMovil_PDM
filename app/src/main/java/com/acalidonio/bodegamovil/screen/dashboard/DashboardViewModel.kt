package com.acalidonio.bodegamovil.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acalidonio.bodegamovil.di.AppContainer
import com.acalidonio.bodegamovil.repository.TokenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import com.acalidonio.bodegamovil.data.remote.ShiftRemoteDataSource
import java.time.LocalDate

data class DashboardUiState(
    val userName: String = "Usuario",
    val currentShift: String = "No hay turno hoy"
)

class DashboardViewModel(
    private val tokenRepository: TokenRepository = AppContainer.tokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            tokenRepository.getUserDetails().collect { user ->
                if (user != null) {
                    _uiState.update { it.copy(userName = user.name) }
                }
            }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val shifts = ShiftRemoteDataSource.getMyWeeklyShifts()
                val today = LocalDate.now().toString()
                val todayShift = shifts.find { it.date == today }

                if (todayShift != null) {
                    val formattedShift = "Turno: ${todayShift.startTime.take(5)} - ${todayShift.endTime.take(5)}"
                    _uiState.update { it.copy(currentShift = formattedShift) }
                } else {
                    _uiState.update { it.copy(currentShift = "No hay turno hoy") }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
