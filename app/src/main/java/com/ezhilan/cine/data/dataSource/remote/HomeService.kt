package com.ezhilan.cine.data.dataSource.remote

import com.ezhilan.cine.data.model.remote.response.AllTrendingListResponse
import com.ezhilan.cine.data.model.remote.response.GenreResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {

    @GET("/3/trending/all/{timeWindow}")
    suspend fun getAllTrending(
        @Path(value = "timeWindow") time: String,
        @Query(value = "language") language: String,
    ): AllTrendingListResponse

    @GET("3/genre/movie/list")
    suspend fun getMovieGeneres(): GenreResponse

    @GET("3/genre/tv/list")
    suspend fun getTvGeneres(): GenreResponse

}