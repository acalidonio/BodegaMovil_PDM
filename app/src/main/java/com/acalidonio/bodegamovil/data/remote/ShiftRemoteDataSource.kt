package com.acalidonio.bodegamovil.data.remote

import com.acalidonio.bodegamovil.data.remote.dto.AssignedShiftDto
import com.acalidonio.bodegamovil.data.remote.dto.GeneralResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

object ShiftRemoteDataSource {
    suspend fun getMyWeeklyShifts(weekOffset: Int = 0): List<AssignedShiftDto> {
        val url = "/api/shifts/me?weekOffset=$weekOffset"
        val response: GeneralResponse<List<AssignedShiftDto>> = ApiClient.client.get(url).body()
        return response.data ?: emptyList()
    }
}
