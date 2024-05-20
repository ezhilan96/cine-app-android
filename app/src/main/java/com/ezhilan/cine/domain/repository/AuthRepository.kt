package com.ezhilan.cine.domain.repository

import com.ezhilan.cine.data.model.remote.request.CreateSessionRequest
import com.ezhilan.cine.data.model.remote.response.CreateRequestTokenResponse
import com.ezhilan.cine.data.model.remote.response.CreateSessionResponse
import com.ezhilan.cine.data.util.DataState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val isSessionActive: Flow<Boolean>

    suspend fun createRequestToken(): Flow<DataState<CreateRequestTokenResponse>>
    suspend fun createSession(createSessionRequest: CreateSessionRequest): Flow<DataState<CreateSessionResponse>>

    fun logout()

}