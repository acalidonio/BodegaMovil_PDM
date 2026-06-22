package com.acalidonio.bodegamovil.model

data class Product(
    val sku: String,
    val name: String,
    val location: String,
    val stock: Int,
    val status: StockStatus,
    val lastAudit: String,
    // Technical specs
    val innerDiameter: String? = null,
    val outerDiameter: String? = null,
    val width: String? = null,
    val weight: String? = null,
    val material: String? = null
)

enum class StockStatus {
    AVAILABLE,
    LOW_STOCK,
    OUT_OF_STOCK
}
