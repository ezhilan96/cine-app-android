package com.ezhilan.cine.data.dataSource.remote

import com.ezhilan.cine.data.model.remote.response.GenreResponse
import com.ezhilan.cine.data.model.remote.response.core.ListResponse
import com.ezhilan.cine.data.model.remote.response.trending.AllTrendingResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingMovieResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingPeopleResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingTvResult
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeService {

    @GET("/3/trending/all/{timeWindow}")
    suspend fun getAllTrending(
        @Path(value = "timeWindow") time: String,
    ): ListResponse<AllTrendingResult>

    @GET("/3/trending/movie/{timeWindow}")
    suspend fun getTrendingMovies(
        @Path(value = "timeWindow") time: String,
    ): ListResponse<TrendingMovieResult>

    @GET("/3/trending/tv/{timeWindow}")
    suspend fun getTrendingTv(
        @Path(value = "timeWindow") time: String,
    ): ListResponse<TrendingTvResult>

    @GET("/3/trending/person/{timeWindow}")
    suspend fun getTrendingPeople(
        @Path(value = "timeWindow") time: String,
    ): ListResponse<TrendingPeopleResult>

    @GET("3/genre/movie/list")
    suspend fun getMovieGenres(): GenreResponse

    @GET("3/genre/tv/list")
    suspend fun getTvGenres(): GenreResponse

}