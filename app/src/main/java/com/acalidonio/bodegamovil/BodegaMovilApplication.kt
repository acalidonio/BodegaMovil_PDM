package com.acalidonio.bodegamovil

import android.app.Application
import com.acalidonio.bodegamovil.di.AppContainer

class BodegaMovilApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.init(this)
    }
}
