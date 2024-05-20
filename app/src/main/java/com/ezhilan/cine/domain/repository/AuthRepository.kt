package com.ezhilan.cine.domain.repository

import com.ezhilan.cine.data.util.DataState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AuthRepository {

    val isLoggedIn: Flow<Boolean>

    suspend fun login(): Flow<DataState<Response<Unit>>>

    fun logout()

}