package com.acalidonio.bodegamovil.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {

    // Phone testing
    // private const val BASE_URL = "http://192.168.0.11:8080"
    // Emulator testing
    private const val BASE_URL = "http://10.0.2.2:8080/"
    // Production
    // private const val BASE_URL = ""

    var authToken: String? = null

    val client = HttpClient(OkHttp) {
        defaultRequest {
            url(BASE_URL)
            authToken?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}