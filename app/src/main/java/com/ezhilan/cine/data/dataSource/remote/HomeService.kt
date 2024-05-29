package com.ezhilan.cine.data.dataSource.remote

import com.ezhilan.cine.data.model.remote.response.core.ListResponse
import com.ezhilan.cine.data.model.remote.response.home.GenreResponse
import com.ezhilan.cine.data.model.remote.response.home.MediaResult
import com.ezhilan.cine.data.model.remote.response.home.MovieResult
import com.ezhilan.cine.data.model.remote.response.home.PeopleResult
import com.ezhilan.cine.data.model.remote.response.home.TvResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {

    @GET("/3/trending/all/{timeWindow}")
    suspend fun getAllTrending(
        @Path(value = "timeWindow") time: String,
        @Query("page") page: Int = 1,
    ): ListResponse<MediaResult>

    @GET("/3/trending/movie/{timeWindow}")
    suspend fun getTrendingMovies(
        @Path(value = "timeWindow") time: String,
        @Query("page") page: Int = 1,
    ): ListResponse<MovieResult>

    @GET("/3/trending/tv/{timeWindow}")
    suspend fun getTrendingTv(
        @Path(value = "timeWindow") time: String,
        @Query("page") page: Int = 1,
    ): ListResponse<TvResult>

    @GET("/3/trending/person/{timeWindow}")
    suspend fun getTrendingPeople(
        @Path(value = "timeWindow") time: String,
        @Query("page") page: Int = 1,
    ): ListResponse<PeopleResult>

    @GET("3/genre/movie/list")
    suspend fun getMovieGenres(): GenreResponse

    @GET("3/genre/tv/list")
    suspend fun getTvGenres(): GenreResponse

    @GET("3/movie/{movieLisType}")
    suspend fun getMovieList(
        @Path(value = "movieLisType") movieListType: String,
        @Query("page") page: Int = 1,
        @Query("region") region: String,
    ): ListResponse<MovieResult>

}