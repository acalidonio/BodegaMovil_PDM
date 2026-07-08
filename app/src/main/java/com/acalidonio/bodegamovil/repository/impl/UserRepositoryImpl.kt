package com.acalidonio.bodegamovil.repository.impl

import com.acalidonio.bodegamovil.data.remote.AuthRemoteDataSource
import com.acalidonio.bodegamovil.data.remote.ServerApiException
import com.acalidonio.bodegamovil.data.remote.dto.LoginRequestDto
import com.acalidonio.bodegamovil.model.User
import com.acalidonio.bodegamovil.model.WorkShift
import com.acalidonio.bodegamovil.repository.TokenRepository
import com.acalidonio.bodegamovil.repository.UserRepository

import kotlinx.coroutines.flow.firstOrNull
import com.acalidonio.bodegamovil.data.remote.ShiftRemoteDataSource
import java.time.LocalDate

class UserRepositoryImpl(
    private val tokenRepository: TokenRepository
) : UserRepository {

    override suspend fun login(employeeId: String, password: String): Boolean {
        return try {
            val response = AuthRemoteDataSource.login(LoginRequestDto(employeeId, password))
            tokenRepository.saveToken(response.token)
            tokenRepository.saveUserDetails(employeeId, response.employeeName, response.initials, response.role, response.profileImageUrl)
            true
        } catch (e: ServerApiException) {
            throw e
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun getUserProfile(): User {
        return tokenRepository.getUserDetails().firstOrNull() ?: User(
            employeeId = "UNKNOWN",
            name = "Usuario Desconocido",
            initials = "NA",
            role = "N/A"
        )
    }

    override suspend fun getWeeklyShifts(weekOffset: Int): List<WorkShift> {
        return try {
            val dtos = ShiftRemoteDataSource.getMyWeeklyShifts(weekOffset)
            val today = LocalDate.now().toString()
            dtos.map { dto ->
                WorkShift(
                    date = dto.date,
                    timeRange = "${dto.startTime.take(5)} - ${dto.endTime.take(5)}",
                    hoursLogged = dto.hoursLogged,
                    isActive = dto.date == today
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}
