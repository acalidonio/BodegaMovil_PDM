package com.acalidonio.bodegamovil.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Login : Routes()

    @Serializable
    data object Dashboard : Routes()
}
