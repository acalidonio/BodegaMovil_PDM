package com.acalidonio.bodegamovil.repository

import com.acalidonio.bodegamovil.model.Product

import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    fun getProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    fun getProductBySku(sku: String): Flow<Product?>
    suspend fun validateAndFetchProduct(sku: String): Product?
    suspend fun createProduct(product: Product)
    suspend fun updateProduct(sku: String, product: Product)
    suspend fun deleteProduct(sku: String)
}
