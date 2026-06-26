package com.acalidonio.bodegamovil.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val employeeId: String,
    val name: String,
    val initials: String,
    val role: String
)