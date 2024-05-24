package com.ezhilan.cine.domain.repository

import com.ezhilan.cine.data.model.remote.response.AllTrendingListResponse
import com.ezhilan.cine.data.model.remote.response.Genre
import com.ezhilan.cine.data.model.remote.response.GenreResponse
import com.ezhilan.cine.data.util.DataState
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    val genres: List<Genre>
    fun getAllTrending(
        timeWindow: String = "day",
        language: String = "en-US",
    ): Flow<DataState<AllTrendingListResponse>>
    fun getTrendingMovies(
        timeWindow: String = "day",
        language: String = "en-US",
    ): Flow<DataState<AllTrendingListResponse>>
    fun getTrendingTv(
        timeWindow: String = "day",
        language: String = "en-US",
    ): Flow<DataState<AllTrendingListResponse>>

    fun getMovieGenres(): Flow<DataState<GenreResponse>>
    fun getTvGenres(): Flow<DataState<GenreResponse>>
}