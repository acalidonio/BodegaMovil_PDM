package com.acalidonio.bodegamovil.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acalidonio.bodegamovil.model.User
import com.acalidonio.bodegamovil.model.WorkShift
import com.acalidonio.bodegamovil.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.acalidonio.bodegamovil.di.AppContainer

data class ProfileUiState(
    val user: User? = null,
    val weeklyShifts: List<WorkShift> = emptyList(),
    val totalHours: Double = 0.0,
    val isLoading: Boolean = true
)

class ProfileViewModel(
    private val repository: UserRepository = AppContainer.userRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = repository.getUserProfile()
            val shifts = repository.getWeeklyShifts()
            val totalHours = shifts.sumOf { it.hoursLogged }
            
            _uiState.update { 
                it.copy(
                    user = user,
                    weeklyShifts = shifts,
                    totalHours = totalHours,
                    isLoading = false
                )
            }
        }
    }
}
