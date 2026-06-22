package com.acalidonio.bodegamovil.model

data class WorkShift(
    val date: String,
    val timeRange: String,
    val hoursLogged: Double,
    val isActive: Boolean = false
)
