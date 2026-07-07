package com.acalidonio.bodegamovil.model

data class User(
    val employeeId: String,
    val name: String,
    val initials: String,
    val role: String,
    val profileImageUrl: String? = null
)
