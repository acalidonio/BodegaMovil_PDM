package com.acalidonio.bodegamovil.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {

    const val X = 2
    private val BASE_URL = when (X) {
        1 -> "http://10.0.2.2:8080/" // Emulator
        2 -> "http://192.168.0.2:8080" // Phone
        else -> "" // Production
    }

    var authToken: String? = null
    var onSessionExpired: (suspend () -> Unit)? = null

    val client = HttpClient(OkHttp) {
        defaultRequest {
            url(BASE_URL)
            authToken?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (response.status == HttpStatusCode.Unauthorized) {
                    onSessionExpired?.invoke()
                }
            }
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