package com.ezhilan.cine.data.repository

import com.ezhilan.cine.data.dataSource.local.dataStore.UserPreferencesDataStore
import com.ezhilan.cine.data.dataSource.remote.AuthService
import com.ezhilan.cine.data.model.remote.request.CreateSessionRequest
import com.ezhilan.cine.data.model.remote.response.CreateRequestTokenResponse
import com.ezhilan.cine.data.model.remote.response.CreateSessionResponse
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.domain.repository.core.NetworkConnectionRepository
import com.ezhilan.cine.domain.repository.core.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    connectionRepo: NetworkConnectionRepository,
    private val authService: AuthService,
    private val dataStore: UserPreferencesDataStore,
) : AuthRepository, RemoteRepository(connectionRepo) {

    override fun handleUnAuthorized() {
        logout()
    }

    override val isSessionActive: Flow<Boolean>
        get() = dataStore.isSessionActive

    override suspend fun createRequestToken(): Flow<DataState<CreateRequestTokenResponse>> =
        executeRemoteCall {
            authService.createRequestToken()
        }.onEach {
            if (it is DataState.Success)
                dataStore.login()
        }

    override suspend fun createSession(createSessionRequest: CreateSessionRequest): Flow<DataState<CreateSessionResponse>> =
        executeRemoteCall {
            authService.createSession(createSessionRequest)
        }


    override fun logout() = dataStore.logout()
}