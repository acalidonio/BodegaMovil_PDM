package com.acalidonio.bodegamovil.data.remote.impl

import com.acalidonio.bodegamovil.data.dto.ProductDto
import com.acalidonio.bodegamovil.data.remote.ApiService


class ApiServiceImpl : ApiService {


    override suspend fun getProducts(): List<ProductDto> {
        return emptyList()
    }


    override suspend fun searchProducts(
        query: String
    ): List<ProductDto> {

        return emptyList()
    }


    override suspend fun getProductBySku(
        sku: String
    ): ProductDto? {

        return null
    }

}