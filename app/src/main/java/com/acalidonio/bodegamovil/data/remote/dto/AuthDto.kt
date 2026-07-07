package com.acalidonio.bodegamovil.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val employeeId: String,
    val password: String
)

@Serializable
data class AuthResponseDto(
    val token: String,
    val employeeName: String,
    val initials: String,
    val role: String,
    val profileImageUrl: String? = null
)
