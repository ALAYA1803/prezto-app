package com.prezto.prezto.di

import com.prezto.prezto.BuildConfig
import com.prezto.prezto.core.util.logging.CrashlyticsLogger
import com.prezto.prezto.core.util.logging.DebugLogger
import com.prezto.prezto.core.util.logging.PreztoLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {

    /**
     * Selecciona la implementación según el tipo de build: Logcat en DEBUG, Crashlytics en RELEASE.
     */
    @Provides
    @Singleton
    fun providePreztoLogger(
        debugLogger: DebugLogger,
        crashlyticsLogger: CrashlyticsLogger
    ): PreztoLogger = if (BuildConfig.DEBUG) debugLogger else crashlyticsLogger
}
