package com.acalidonio.bodegamovil.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkShiftDto(
    val date: String,
    val timeRange: String,
    val hoursLogged: Double,
    val isActive: Boolean = false
)