package com.acalidonio.bodegamovil.data.remote

import com.acalidonio.bodegamovil.BuildConfig
import com.acalidonio.bodegamovil.data.remote.dto.ApiErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.client.call.body
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {

    private const val BASE_URL = BuildConfig.BASE_URL

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
                if (!response.status.isSuccess() && response.status != HttpStatusCode.Unauthorized) {
                    try {
                        val errorResponse = response.body<ApiErrorResponse>()
                        throw ServerApiException(errorResponse.getReadableMessage())
                    } catch (e: Exception) {
                        if (e is ServerApiException) throw e
                    }
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