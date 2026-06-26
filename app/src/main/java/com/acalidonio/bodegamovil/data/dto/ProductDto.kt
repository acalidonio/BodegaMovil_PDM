package com.acalidonio.bodegamovil.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val sku: String,
    val name: String,
    val location: String,
    val stock: Int,
    val status: String,
    val lastAudit: String
)