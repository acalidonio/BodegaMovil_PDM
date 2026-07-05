package com.acalidonio.bodegamovil.data.remote

import com.acalidonio.bodegamovil.data.remote.dto.AuthResponseDto
import com.acalidonio.bodegamovil.data.remote.dto.GeneralResponse
import com.acalidonio.bodegamovil.data.remote.dto.LoginRequestDto
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object AuthRemoteDataSource {
    suspend fun login(request: LoginRequestDto): AuthResponseDto {
        val response: GeneralResponse<AuthResponseDto> = ApiClient.client.post("/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        return response.data ?: throw Exception(response.message)
    }
}
