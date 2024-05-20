package com.ezhilan.cine.data.dataSource.remote

import com.ezhilan.cine.data.model.remote.response.AllTrendingListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeService {

    @GET("/trending/all/day")
    fun getAllTrending(
        @Query(
            value = "language",
            encoded = true,
        ) language: String = "en-US"
    ): AllTrendingListResponse
}