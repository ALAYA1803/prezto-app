package com.prezto.prezto.di

import com.prezto.prezto.core.data.location.FusedLocationProvider
import com.prezto.prezto.core.data.location.LocationProvider
import com.prezto.prezto.feature_explore.data.repository.MockMapRepositoryImpl
import com.prezto.prezto.feature_explore.domain.repository.MapRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(impl: FusedLocationProvider): LocationProvider

    @Binds
    @Singleton
    abstract fun bindMapRepository(impl: MockMapRepositoryImpl): MapRepository
}
