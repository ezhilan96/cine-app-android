package com.ezhilan.cine.domain.repository.core

import com.ezhilan.cine.data.repository.core.ConnectionState
import kotlinx.coroutines.flow.Flow

interface NetworkConnectionRepository {

    val connectionState: Flow<ConnectionState>

    fun checkConnection()
}