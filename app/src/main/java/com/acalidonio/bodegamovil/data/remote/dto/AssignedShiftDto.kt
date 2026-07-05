package com.acalidonio.bodegamovil.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AssignedShiftDto(
    val name: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val hoursLogged: Double
)
