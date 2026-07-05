package com.acalidonio.bodegamovil.data.remote

import com.acalidonio.bodegamovil.data.remote.dto.GeneralResponse
import com.acalidonio.bodegamovil.data.remote.dto.PageableResponse
import com.acalidonio.bodegamovil.data.remote.dto.ProductDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object InventoryRemoteDataSource {

    private const val BASE_URL : String = "/api/inventory"
    suspend fun fetchProducts(query: String? = null): List<ProductDto> {
        val response: GeneralResponse<PageableResponse<ProductDto>> = ApiClient.client.get(BASE_URL) {
            if (!query.isNullOrBlank()) {
                parameter("query", query)
            }
        }.body()
        
        return response.data?.content ?: emptyList()
    }

    suspend fun createProduct(product: ProductDto): ProductDto {
        val response: GeneralResponse<ProductDto> = ApiClient.client.post(BASE_URL) {
            contentType(ContentType.Application.Json)
            setBody(product)
        }.body()
        return response.data ?: throw Exception(response.message)
    }

    suspend fun getProduct(sku: String): ProductDto? {
        val response: GeneralResponse<ProductDto> = ApiClient.client.get("$BASE_URL/$sku").body()
        return response.data
    }

    suspend fun updateProduct(sku: String, product: ProductDto): ProductDto {
        val response: GeneralResponse<ProductDto> = ApiClient.client.put("$BASE_URL/$sku") {
            contentType(ContentType.Application.Json)
            setBody(product)
        }.body()
        return response.data ?: throw Exception(response.message)
    }

    suspend fun deleteProduct(sku: String) {
        ApiClient.client.delete("$BASE_URL/$sku")
    }
}
