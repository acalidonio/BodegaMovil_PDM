package com.acalidonio.bodegamovil.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class ApiErrorResponse(
    val status: Int,
    val message: JsonElement,
    val time: String,
    val uri: String
) {
    fun getReadableMessage(): String {
        return try {
            if (message is JsonObject) {
                message.values.joinToString("\n") { it.toString().replace("\"", "") }
            } else {
                message.toString().replace("\"", "")
            }
        } catch (_: Exception) {
            "Ocurrió un error inesperado en el servidor."
        }
    }
}
