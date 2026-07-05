package com.acalidonio.bodegamovil.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageableResponse<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)
