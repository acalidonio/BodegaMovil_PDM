package com.acalidonio.bodegamovil.repository.impl

import com.acalidonio.bodegamovil.dummy.DummyWarehouseData
import com.acalidonio.bodegamovil.model.User
import com.acalidonio.bodegamovil.model.WorkShift
import com.acalidonio.bodegamovil.repository.UserRepository
import com.acalidonio.bodegamovil.data.remote.ApiService

class UserRepositoryImpl(
    private val apiService: ApiService? = null
) : UserRepository {
    override suspend fun getUserProfile(): User {
        return DummyWarehouseData.sampleUser
    }

    override suspend fun getWeeklyShifts(): List<WorkShift> {
        return DummyWarehouseData.sampleShifts
    }
}
