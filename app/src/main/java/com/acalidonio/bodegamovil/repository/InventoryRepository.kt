package com.acalidonio.bodegamovil.repository

import com.acalidonio.bodegamovil.model.DashboardStats
import com.acalidonio.bodegamovil.model.Product

import com.acalidonio.bodegamovil.model.ProductCategory
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    fun getProducts(): Flow<List<Product>>
    fun searchProducts(query: String, categories: Set<ProductCategory> = emptySet()): Flow<List<Product>>
    suspend fun syncProducts(query: String, categories: Set<ProductCategory> = emptySet(), page: Int = 0): Int
    fun getProductBySku(sku: String): Flow<Product?>
    suspend fun validateAndFetchProduct(sku: String): Product?
    suspend fun createProduct(product: Product)
    suspend fun updateProduct(sku: String, product: Product)
    suspend fun deleteProduct(sku: String)
    suspend fun getDashboardStats(): DashboardStats
    suspend fun getRecentProducts(): List<Product>
}
