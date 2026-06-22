package com.acalidonio.bodegamovil.repository.impl

import com.acalidonio.bodegamovil.dummy.DummyWarehouseData
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.repository.InventoryRepository

class InventoryRepositoryImpl : InventoryRepository {
    override suspend fun getProducts(): List<Product> {
        return DummyWarehouseData.sampleProducts
    }

    override suspend fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return DummyWarehouseData.sampleProducts
        
        return DummyWarehouseData.sampleProducts.filter {
            it.name.contains(query, ignoreCase = true) || 
            it.sku.contains(query, ignoreCase = true) ||
            it.location.contains(query, ignoreCase = true)
        }
    }

    override suspend fun getProductBySku(sku: String): Product? {
        return DummyWarehouseData.sampleProducts.find { it.sku == sku }
    }
}
