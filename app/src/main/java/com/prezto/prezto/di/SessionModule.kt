package com.prezto.prezto.di

import com.prezto.prezto.core.data.session.SessionManager
import com.prezto.prezto.core.data.session.SessionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {

    @Binds
    @Singleton
    abstract fun bindSessionManager(impl: SessionManagerImpl): SessionManager
}
