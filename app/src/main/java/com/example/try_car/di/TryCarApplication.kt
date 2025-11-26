package com.example.try_car.di

import android.app.Application
import com.example.data.di.DataModule
import com.example.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class TryCarApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@TryCarApplication)
            modules(DataModule().module, PresentationModule().module)
        }
    }
}