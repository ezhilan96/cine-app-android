package com.ezhilan.cine.core.di

import com.ezhilan.cine.data.repository.AuthRepositoryImpl
import com.ezhilan.cine.data.repository.HomeRepositoryImpl
import com.ezhilan.cine.data.repository.core.NetworkConnectionRepositoryImpl
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.domain.repository.core.NetworkConnectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindNetworkConnectionRepository(repositoryImpl: NetworkConnectionRepositoryImpl): NetworkConnectionRepository

}