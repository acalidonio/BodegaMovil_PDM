package com.acalidonio.bodegamovil.repository

import com.acalidonio.bodegamovil.model.User
import com.acalidonio.bodegamovil.model.WorkShift

interface UserRepository {
    suspend fun getUserProfile(): User
    suspend fun getWeeklyShifts(): List<WorkShift>
}
