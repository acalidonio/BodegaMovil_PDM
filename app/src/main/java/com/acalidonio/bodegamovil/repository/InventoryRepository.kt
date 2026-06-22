package com.acalidonio.bodegamovil.repository

import com.acalidonio.bodegamovil.model.Product

interface InventoryRepository {
    suspend fun getProducts(): List<Product>
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getProductBySku(sku: String): Product?
}
