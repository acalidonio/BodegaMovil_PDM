package com.acalidonio.bodegamovil.di

import com.acalidonio.bodegamovil.repository.InventoryRepository
import com.acalidonio.bodegamovil.repository.UserRepository
import com.acalidonio.bodegamovil.repository.impl.InventoryRepositoryImpl
import com.acalidonio.bodegamovil.repository.impl.UserRepositoryImpl

object AppContainer {

    val inventoryRepository: InventoryRepository =
        InventoryRepositoryImpl()

    val userRepository: UserRepository =
        UserRepositoryImpl()
}