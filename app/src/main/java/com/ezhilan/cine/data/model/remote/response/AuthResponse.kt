package com.ezhilan.cine.data.model.remote.response

data class CreateRequestTokenResponse(
    val expires_at: String?,
    val request_token: String?,
    val success: Boolean?
)

data class CreateSessionResponse(
    val session_id: String?,
    val success: Boolean?
)