package com.ezhilan.cine.data.dataSource.remote

import com.ezhilan.cine.data.model.remote.response.GenreResponse
import com.ezhilan.cine.data.model.remote.response.core.ListResponse
import com.ezhilan.cine.data.model.remote.response.trending.AllTrendingResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingMovieResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingPeopleResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingTvResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {

    @GET("/3/trending/all/{timeWindow}")
    suspend fun getAllTrending(
        @Path(value = "timeWindow") time: String,
        @Query("page") page: Int = 1,
    ): ListResponse<AllTrendingResult>

    @GET("/3/trending/movie/{timeWindow}")
    suspend fun getTrendingMovies(
        @Path(value = "timeWindow") time: String,
        @Query("page") page: Int = 1,
    ): ListResponse<TrendingMovieResult>

    @GET("/3/trending/tv/{timeWindow}")
    suspend fun getTrendingTv(
        @Path(value = "timeWindow") time: String,
        @Query("page") page: Int = 1,
    ): ListResponse<TrendingTvResult>

    @GET("/3/trending/person/{timeWindow}")
    suspend fun getTrendingPeople(
        @Path(value = "timeWindow") time: String,
        @Query("page") page: Int = 1,
    ): ListResponse<TrendingPeopleResult>

    @GET("3/genre/movie/list")
    suspend fun getMovieGenres(): GenreResponse

    @GET("3/genre/tv/list")
    suspend fun getTvGenres(): GenreResponse

}