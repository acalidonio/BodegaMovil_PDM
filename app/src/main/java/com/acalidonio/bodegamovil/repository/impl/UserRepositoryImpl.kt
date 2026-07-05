package com.acalidonio.bodegamovil.repository.impl

import com.acalidonio.bodegamovil.data.remote.AuthRemoteDataSource
import com.acalidonio.bodegamovil.data.remote.dto.LoginRequestDto
import com.acalidonio.bodegamovil.model.User
import com.acalidonio.bodegamovil.model.WorkShift
import com.acalidonio.bodegamovil.repository.TokenRepository
import com.acalidonio.bodegamovil.repository.UserRepository

import kotlinx.coroutines.flow.firstOrNull

class UserRepositoryImpl(
    private val tokenRepository: TokenRepository
) : UserRepository {

    override suspend fun login(employeeId: String, password: String): Boolean {
        return try {
            val response = AuthRemoteDataSource.login(LoginRequestDto(employeeId, password))
            tokenRepository.saveToken(response.token)
            tokenRepository.saveUserDetails(employeeId, response.employeeName, response.role)
            true
        } catch (e: Exception) {
            e.printStackTrace()
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

    override suspend fun getWeeklyShifts(): List<WorkShift> {
        // Since backend doesn't have shifts yet, we will return empty list or keep it dummy.
        // For offline-first realism without backend support, an empty list is appropriate.
        return emptyList()
    }
}
