package com.acalidonio.bodegamovil.data.remote.dto

import com.acalidonio.bodegamovil.data.local.entity.ProductEntity
import com.acalidonio.bodegamovil.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val sku: String,
    val name: String,
    val description: String? = null,
    val status: String,
    val location: String,
    val stock: Int,
    val lastAudit: String? = null,
    val innerDiameter: String? = null,
    val outerDiameter: String? = null,
    val width: String? = null,
    val weight: String? = null,
    val material: String? = null,
    val imageUrl: String? = null,
    val category: String? = null
)

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        sku = sku,
        name = name,
        description = description,
        location = location,
        stock = stock,
        status = status,
        lastAudit = lastAudit ?: "N/A",
        innerDiameter = innerDiameter,
        outerDiameter = outerDiameter,
        width = width,
        weight = weight,
        material = material,
        imageUrl = imageUrl,
        category = category
    )
}

fun Product.toDto(): ProductDto {
    return ProductDto(
        sku = sku,
        name = name,
        description = description,
        location = location,
        stock = stock,
        status = status.name,
        lastAudit = lastAudit,
        innerDiameter = innerDiameter,
        outerDiameter = outerDiameter,
        width = width,
        weight = weight,
        material = material,
        imageUrl = imageUrl,
        category = category?.name
    )
}
