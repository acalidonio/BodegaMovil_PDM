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
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

data class ProfileUiState(
    val user: User? = null,
    val weeklyShifts: List<WorkShift> = emptyList(),
    val totalHours: Double = 0.0,
    val isLoading: Boolean = true,
    val isShiftsLoading: Boolean = false,
    val weekOffset: Int = 0,
    val weekLabelText: String = "Esta Semana"
)

class ProfileViewModel(
    private val repository: UserRepository = AppContainer.userRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    private var currentWeekOffset = 0

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = repository.getUserProfile()
            val shifts = repository.getWeeklyShifts(currentWeekOffset)
            val totalHours = shifts.sumOf { it.hoursLogged }
            
            _uiState.update { 
                it.copy(
                    user = user,
                    weeklyShifts = shifts,
                    totalHours = totalHours,
                    isLoading = false,
                    weekOffset = currentWeekOffset,
                    weekLabelText = getWeekLabel(currentWeekOffset)
                )
            }
        }
    }

    private fun loadShiftsData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isShiftsLoading = true) }
            val shifts = repository.getWeeklyShifts(currentWeekOffset)
            val totalHours = shifts.sumOf { it.hoursLogged }
            
            _uiState.update { 
                it.copy(
                    weeklyShifts = shifts,
                    totalHours = totalHours,
                    isShiftsLoading = false,
                    weekOffset = currentWeekOffset,
                    weekLabelText = getWeekLabel(currentWeekOffset)
                )
            }
        }
    }

    fun nextWeek() {
        currentWeekOffset++
        loadShiftsData()
    }

    fun previousWeek() {
        currentWeekOffset--
        loadShiftsData()
    }

    fun jumpToDate(dateMillis: Long?) {
        if (dateMillis == null) return
        val selectedDate = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()
        val mondayToday = today.with(DayOfWeek.MONDAY)
        val mondaySelected = selectedDate.with(DayOfWeek.MONDAY)
        val offset = ChronoUnit.WEEKS.between(mondayToday, mondaySelected).toInt()
        
        if (offset != currentWeekOffset) {
            currentWeekOffset = offset
            loadShiftsData()
        }
    }

    private fun getWeekLabel(offset: Int): String {
        return when (offset) {
            1 -> "Próxima Semana"
            0 -> "Esta Semana"
            -1 -> "Semana Pasada"
            else -> {
                val targetDate = LocalDate.now().plusWeeks(offset.toLong())
                val startOfWeek = targetDate.with(DayOfWeek.MONDAY)
                val endOfWeek = targetDate.with(DayOfWeek.SUNDAY)
                val formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.forLanguageTag("es-ES"))
                "${startOfWeek.format(formatter)} - ${endOfWeek.format(formatter)}"
            }
        }
    }

    fun refresh() {
        loadInitialData()
    }
}
