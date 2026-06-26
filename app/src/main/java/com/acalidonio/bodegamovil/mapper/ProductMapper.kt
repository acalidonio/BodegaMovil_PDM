package com.acalidonio.bodegamovil.mapper

import com.acalidonio.bodegamovil.data.dto.ProductDto
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.model.StockStatus


fun ProductDto.toProduct(): Product {

    return Product(
        sku = sku,
        name = name,
        location = location,
        stock = stock,
        status = when(status){
            "AVAILABLE" -> StockStatus.AVAILABLE
            "LOW_STOCK" -> StockStatus.LOW_STOCK
            "OUT_OF_STOCK" -> StockStatus.OUT_OF_STOCK
            else -> StockStatus.AVAILABLE
        },
        lastAudit = lastAudit
    )
}