package com.acalidonio.bodegamovil.repository

import kotlinx.coroutines.flow.Flow
import com.acalidonio.bodegamovil.model.User

interface TokenRepository {
    suspend fun saveToken(token: String)
    suspend fun saveUserDetails(id: String, name: String, initials: String, role: String)
    suspend fun clearToken()
    fun getToken(): Flow<String?>
    fun getUserDetails(): Flow<User?>
}
