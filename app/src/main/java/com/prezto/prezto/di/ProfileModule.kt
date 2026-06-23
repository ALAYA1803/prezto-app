package com.prezto.prezto.di

import com.prezto.prezto.feature_profile.data.repository.MockProfileRepositoryImpl
import com.prezto.prezto.feature_profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: MockProfileRepositoryImpl): ProfileRepository
}
