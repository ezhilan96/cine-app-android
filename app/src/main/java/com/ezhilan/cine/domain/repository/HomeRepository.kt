package com.ezhilan.cine.domain.repository

import com.ezhilan.cine.data.model.remote.response.Genre
import com.ezhilan.cine.data.model.remote.response.GenreResponse
import com.ezhilan.cine.data.model.remote.response.core.ListResponse
import com.ezhilan.cine.data.model.remote.response.trending.AllTrendingResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingMovieResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingPeopleResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingTvResult
import com.ezhilan.cine.data.util.DataState
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    val genres: List<Genre>
    fun setGenres(genres: List<Genre>)

    fun getAllTrending(
        page: Int = 1,
        timeWindow: String = "day"
    ): Flow<DataState<ListResponse<AllTrendingResult>>>

    fun getTrendingMovies(
        page: Int = 1,
        timeWindow: String = "day"
    ): Flow<DataState<ListResponse<TrendingMovieResult>>>

    fun getTrendingTv(
        page: Int = 1,
        timeWindow: String = "day"
    ): Flow<DataState<ListResponse<TrendingTvResult>>>

    fun getTrendingPeople(
        page: Int = 1,
        timeWindow: String = "day"
    ): Flow<DataState<ListResponse<TrendingPeopleResult>>>

    fun getMovieGenres(): Flow<DataState<GenreResponse>>
    fun getTvGenres(): Flow<DataState<GenreResponse>>
}