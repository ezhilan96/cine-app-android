package com.ezhilan.cine.data.dataSource.remote

import retrofit2.Response
import retrofit2.http.POST

interface HomeService {

    @POST("/logout")
    suspend fun logout(): Response<Unit>
}