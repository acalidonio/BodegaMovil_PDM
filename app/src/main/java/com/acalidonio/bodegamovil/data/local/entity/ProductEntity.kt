package com.acalidonio.bodegamovil.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.model.StockStatus

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val sku: String,
    val name: String,
    val location: String,
    val stock: Int,
    val status: String,
    val lastAudit: String,
    val innerDiameter: String? = null,
    val outerDiameter: String? = null,
    val width: String? = null,
    val weight: String? = null,
    val material: String? = null,
    val imageUrl: String? = null
)

fun ProductEntity.toDomain(): Product {
    return Product(
        sku = sku,
        name = name,
        location = location,
        stock = stock,
        status = try { StockStatus.valueOf(status) } catch (_: Exception) { StockStatus.AVAILABLE },
        lastAudit = lastAudit,
        innerDiameter = innerDiameter,
        outerDiameter = outerDiameter,
        width = width,
        weight = weight,
        material = material,
        imageUrl = imageUrl
    )
}

