package com.acalidonio.bodegamovil.di

import android.content.Context
import com.acalidonio.bodegamovil.data.local.BodegaDatabase
import com.acalidonio.bodegamovil.data.remote.ApiClient
import com.acalidonio.bodegamovil.repository.InventoryRepository
import com.acalidonio.bodegamovil.repository.TokenRepository
import com.acalidonio.bodegamovil.repository.UserRepository
import com.acalidonio.bodegamovil.repository.impl.InventoryRepositoryImpl
import com.acalidonio.bodegamovil.repository.impl.TokenRepositoryImpl
import com.acalidonio.bodegamovil.repository.impl.UserRepositoryImpl

object AppContainer {

    lateinit var tokenRepository: TokenRepository
        private set

    lateinit var database: BodegaDatabase
        private set

    lateinit var inventoryRepository: InventoryRepository
        private set

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(tokenRepository)
    }

    fun init(context: Context) {
        tokenRepository = TokenRepositoryImpl(context)
        database = BodegaDatabase.getDatabase(context)
        inventoryRepository = InventoryRepositoryImpl(database.productDao())
        
        ApiClient.onSessionExpired = {
            tokenRepository.clearToken()
            ApiClient.authToken = null
        }
    }
}