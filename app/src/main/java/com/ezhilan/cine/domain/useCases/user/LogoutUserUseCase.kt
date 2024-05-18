package com.ezhilan.cine.domain.useCases.user

import com.ezhilan.cine.data.dataSource.local.dataStore.UserPreferencesDataStore
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.repository.AuthRepository
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val dataStore: UserPreferencesDataStore,
    private val repo: AuthRepository,
) {

    operator fun invoke() = repo.logout().onEach {
        if (it is DataState.Success) {
            dataStore.logout()
        }
    }
}