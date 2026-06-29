package com.acalidonio.bodegamovil.di

import android.content.Context
import com.acalidonio.bodegamovil.repository.InventoryRepository
import com.acalidonio.bodegamovil.repository.TokenRepository
import com.acalidonio.bodegamovil.repository.UserRepository
import com.acalidonio.bodegamovil.repository.impl.InventoryRepositoryImpl
import com.acalidonio.bodegamovil.repository.impl.TokenRepositoryImpl
import com.acalidonio.bodegamovil.repository.impl.UserRepositoryImpl

object AppContainer {

    lateinit var tokenRepository: TokenRepository
        private set

    val inventoryRepository: InventoryRepository by lazy {
        InventoryRepositoryImpl()
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl()
    }

    fun init(context: Context) {
        tokenRepository = TokenRepositoryImpl(context)
    }
}