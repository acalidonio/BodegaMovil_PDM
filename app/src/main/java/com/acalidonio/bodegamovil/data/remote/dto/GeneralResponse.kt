package com.acalidonio.bodegamovil.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeneralResponse<T>(
    val message: String,
    val data: T? = null
)
