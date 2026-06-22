package com.acalidonio.bodegamovil.dummy

import com.acalidonio.bodegamovil.model.Product
import com.acalidonio.bodegamovil.model.StockStatus
import com.acalidonio.bodegamovil.model.User
import com.acalidonio.bodegamovil.model.WorkShift

object DummyWarehouseData {
    val sampleProducts = listOf(
        Product(
            sku = "SKU-84729",
            name = "Cylindrical Roller Bearing NU 205",
            location = "Pasillo 4, Estante B",
            stock = 45,
            status = StockStatus.AVAILABLE,
            lastAudit = "10/06/26",
            innerDiameter = "25 mm",
            outerDiameter = "52 mm",
            width = "15 mm",
            weight = "0.13 kg",
            material = "Chrome Steel"
        ),
        Product(
            sku = "SKU-84730",
            name = "Deep Groove Ball Bearing 6205",
            location = "Pasillo 4, Estante C",
            stock = 0,
            status = StockStatus.OUT_OF_STOCK,
            lastAudit = "12/06/26"
        ),
        Product(
            sku = "SKU-84731",
            name = "Tapered Roller Bearing 30205",
            location = "Pasillo 5, Estante A",
            stock = 3,
            status = StockStatus.LOW_STOCK,
            lastAudit = "14/06/26"
        ),
        Product(
            sku = "SKU-84732",
            name = "Spherical Roller Bearing 22205",
            location = "Pasillo 3, Estante D",
            stock = 64,
            status = StockStatus.AVAILABLE,
            lastAudit = "06/06/26"
        )
    )

    val sampleUser = User(
        employeeId = "EMP-4729",
        name = "André C.",
        initials = "AC",
        role = "Operador de Bodega"
    )

    val sampleShifts = listOf(
        WorkShift(date = "Lunes 15 de Junio", timeRange = "08:00 AM - 05:00 PM", hoursLogged = 8.0),
        WorkShift(date = "Martes 16 de Junio", timeRange = "08:00 AM - 12:00 PM", hoursLogged = 4.0, isActive = true),
        WorkShift(date = "Miercoles 17 de Junio", timeRange = "08:00 AM - 05:00 PM", hoursLogged = 8.0),
        WorkShift(date = "Jueves 18 de Junio", timeRange = "08:00 AM - 05:00 PM", hoursLogged = 8.0),
        WorkShift(date = "Viernes 19 de Junio", timeRange = "08:00 AM - 05:00 PM", hoursLogged = 8.0)
    )
}
