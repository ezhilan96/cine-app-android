package com.ezhilan.cine.data.dataSource.remote

import com.ezhilan.cine.data.model.remote.request.CreateSessionRequest
import com.ezhilan.cine.data.model.remote.response.CreateRequestTokenResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface AuthService {

    @GET("/authentication/token/new")
    fun createRequestToken(): CreateRequestTokenResponse

    @GET("/authentication/token/validate_with_login")
    fun createSession(@Body createSessionRequest: CreateSessionRequest): CreateRequestTokenResponse

}

