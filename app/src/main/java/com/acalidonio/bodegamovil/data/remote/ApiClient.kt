package com.acalidonio.bodegamovil.data.remote

import com.acalidonio.bodegamovil.di.AppContainer
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json


object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:8080"

    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        
        install(Auth) {
            bearer {
                loadTokens {
                    val token = AppContainer.tokenRepository.getToken().firstOrNull()
                    if (token != null) BearerTokens(token, "") else null
                }
            }
        }
    }
}