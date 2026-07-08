package com.acalidonio.bodegamovil.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    fun isTokenValid(token: String?): Boolean {
        if (token.isNullOrBlank()) return false

        try {
            val parts = token.split(".")
            if (parts.size != 3) return false

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val jsonObject = JSONObject(payload)

            if (!jsonObject.has("exp")) return false

            val exp = jsonObject.getLong("exp")
            val currentTimeSeconds = System.currentTimeMillis() / 1000

            return exp > currentTimeSeconds
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
