package com.acalidonio.bodegamovil.repository.impl

import com.acalidonio.bodegamovil.data.local.dao.ProductDao
import com.acalidonio.bodegamovil.data.local.entity.toDomain
import com.acalidonio.bodegamovil.data.remote.InventoryRemoteDataSource
import com.acalidonio.bodegamovil.data.remote.dto.toDto
import com.acalidonio.bodegamovil.data.remote.dto.toEntity
import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.model.ProductCategory
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

    override fun searchProducts(query: String, categories: Set<ProductCategory>): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            var domainList = entities.map { it.toDomain() }
            if (categories.isNotEmpty()) {
                domainList = domainList.filter { it.category in categories }
            }
            domainList
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

    override suspend fun validateAndFetchProduct(sku: String): Product? {
        val localProduct = productDao.getProductBySkuSync(sku)
        if (localProduct != null) {
            return localProduct.toDomain()
        }

        return try {
            val remoteProduct = InventoryRemoteDataSource.getProduct(sku)
            if (remoteProduct != null) {
                val entity = remoteProduct.toEntity()
                productDao.insertProduct(entity)
                entity.toDomain()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun syncProducts(query: String, categories: Set<ProductCategory>) {
        try {
            val remoteProducts = InventoryRemoteDataSource.fetchProducts(query, categories.map { it.name }.toSet())
            val entities = remoteProducts.map { it.toEntity() }
            
            if (query.isBlank()) {
                productDao.replaceAll(entities)
            } else {
                productDao.insertProducts(entities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun refreshProductsFromServer(query: String? = null, categories: Set<String>? = null) {
        repositoryScope.launch {
            try {
                val remoteProducts = InventoryRemoteDataSource.fetchProducts(query, categories)
                val entities = remoteProducts.map { it.toEntity() }
                
                if (query.isNullOrBlank()) {
                    productDao.deleteAll()
                }
                
                productDao.insertProducts(entities)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
