package com.ezhilan.cine.data.repository

import com.ezhilan.cine.core.di.IoDispatcher
import com.ezhilan.cine.data.dataSource.local.dataStore.UserPreferencesDataStore
import com.ezhilan.cine.data.dataSource.remote.AuthService
import com.ezhilan.cine.data.dataSource.remote.HomeService
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.domain.repository.core.NetworkConnectionRepository
import com.ezhilan.cine.domain.repository.core.RemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    connectionRepo: NetworkConnectionRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val authService: AuthService,
    private val homeService: HomeService,
    private val dataStore: UserPreferencesDataStore,
) : AuthRepository, RemoteRepository(connectionRepo) {

    override fun handleUnAuthorized() {
        logout()
    }

    override val isLoggedIn: Flow<Boolean>
        get() = dataStore.isLoggedIn

    override suspend fun login(): Flow<DataState<Response<Unit>>> =
        flowOf(DataState.Success(Response.success(Unit)))
//    executeRemoteCall {
//        authService.getOtp()
//    }
            .onEach {
                if (it is DataState.Success)
                    dataStore.login()
            }


    override fun logout() = dataStore.logout()
}