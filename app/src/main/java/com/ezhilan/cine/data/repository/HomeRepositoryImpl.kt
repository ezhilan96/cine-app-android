package com.ezhilan.cine.data.repository

import com.ezhilan.cine.core.di.IoDispatcher
import com.ezhilan.cine.data.dataSource.local.dataStore.UserPreferencesDataStore
import com.ezhilan.cine.data.dataSource.remote.HomeService
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.domain.repository.core.NetworkConnectionRepository
import com.ezhilan.cine.domain.repository.core.RemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    connectionRepo: NetworkConnectionRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val authRepo: AuthRepository,
    private val homeService: HomeService,
    private val dataStore: UserPreferencesDataStore,
) : HomeRepository, RemoteRepository(connectionRepo) {
    override fun handleUnAuthorized() {
        authRepo.logout()
    }

}