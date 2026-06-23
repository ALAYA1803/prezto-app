package com.prezto.prezto

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PreztoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}