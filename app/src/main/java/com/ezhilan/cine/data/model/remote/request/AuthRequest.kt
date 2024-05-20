package com.ezhilan.cine.data.model.remote.request

data class CreateSessionRequest(
    val password: String?,
    val request_token: String?,
    val username: String?
)