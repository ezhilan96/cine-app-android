package com.ezhilan.cine.data.dataSource.remote

import com.ezhilan.cine.data.model.remote.request.CreateSessionRequest
import com.ezhilan.cine.data.model.remote.response.CreateRequestTokenResponse
import com.ezhilan.cine.data.model.remote.response.CreateSessionResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface AuthService {

    @GET("/authentication/token/new")
    fun createRequestToken(): CreateRequestTokenResponse

    @GET("/authentication/session/new")
    fun createSession(@Body createSessionRequest: CreateSessionRequest): CreateSessionResponse

}

