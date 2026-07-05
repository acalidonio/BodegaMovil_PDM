package com.acalidonio.bodegamovil.repository.impl

import com.acalidonio.bodegamovil.data.local.dao.ProductDao
import com.acalidonio.bodegamovil.data.local.entity.toDomain
import com.acalidonio.bodegamovil.data.remote.InventoryRemoteDataSource
import com.acalidonio.bodegamovil.data.remote.dto.toDto
import com.acalidonio.bodegamovil.data.remote.dto.toEntity
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.repository.InventoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class InventoryRepositoryImpl(
    private val productDao: ProductDao
) : InventoryRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    override fun getProducts(): Flow<List<Product>> {
        refreshProductsFromServer()
        return productDao.searchProducts("").map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        refreshProductsFromServer(query)
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getProductBySku(sku: String): Flow<Product?> {
        repositoryScope.launch {
            try {
                val remoteProduct = InventoryRemoteDataSource.getProduct(sku)
                if (remoteProduct != null) {
                    productDao.insertProduct(remoteProduct.toEntity())
                }
            } catch (_: Exception) {}
        }
        return productDao.getProductBySku(sku).map { it?.toDomain() }
    }

    private fun refreshProductsFromServer(query: String? = null) {
        repositoryScope.launch {
            try {
                val remoteProducts = InventoryRemoteDataSource.fetchProducts(query)
                val entities = remoteProducts.map { it.toEntity() }
                
                if (query.isNullOrBlank()) {
                    productDao.deleteAll()
                }
                
                productDao.insertProducts(entities)
            } catch (_: Exception) {}
        }
    }

    override suspend fun createProduct(product: Product) {
        val remoteSaved = InventoryRemoteDataSource.createProduct(product.toDto())
        productDao.insertProduct(remoteSaved.toEntity())
    }

    override suspend fun updateProduct(sku: String, product: Product) {
        val remoteSaved = InventoryRemoteDataSource.updateProduct(sku, product.toDto())
        productDao.insertProduct(remoteSaved.toEntity())
    }

    override suspend fun deleteProduct(sku: String) {
        InventoryRemoteDataSource.deleteProduct(sku)
        productDao.deleteProductBySku(sku)
    }
}
