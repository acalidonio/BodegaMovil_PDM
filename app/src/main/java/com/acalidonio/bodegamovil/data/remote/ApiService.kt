package com.acalidonio.bodegamovil.data.remote

import com.acalidonio.bodegamovil.data.dto.ProductDto


interface ApiService {

    suspend fun getProducts(): List<ProductDto>

    suspend fun searchProducts(
        query: String
    ): List<ProductDto>

    suspend fun getProductBySku(
        sku: String
    ): ProductDto?

}