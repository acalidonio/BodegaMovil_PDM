package com.acalidonio.bodegamovil.data.remote.dto

import kotlinx.serialization.Serializable
import com.acalidonio.bodegamovil.model.DashboardStats

@Serializable
data class DashboardStatsDto(
    val totalProducts: Long,
    val lowStockProducts: Long,
    val totalStockItems: Long
) {
    fun toDomain(): DashboardStats {
        return DashboardStats(
            totalProducts = totalProducts,
            lowStockProducts = lowStockProducts,
            totalStockItems = totalStockItems
        )
    }
}
