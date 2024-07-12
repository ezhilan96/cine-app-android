package com.ezhilan.cine.domain.repository

import com.ezhilan.cine.data.model.remote.response.core.ListResponse
import com.ezhilan.cine.data.model.remote.response.home.Genre
import com.ezhilan.cine.data.model.remote.response.home.GenreResponse
import com.ezhilan.cine.data.model.remote.response.home.MediaResult
import com.ezhilan.cine.data.model.remote.response.home.MovieResult
import com.ezhilan.cine.data.model.remote.response.home.PeopleResult
import com.ezhilan.cine.data.model.remote.response.home.TvResult
import com.ezhilan.cine.data.util.DataState
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    val genres: List<Genre>
    fun setGenres(genres: List<Genre>)

    fun getAllTrending(
        page: Int = 1, timeWindow: String = "day"
    ): Flow<DataState<ListResponse<MediaResult>>>

    fun getTrendingMovies(
        page: Int = 1, timeWindow: String = "day"
    ): Flow<DataState<ListResponse<MovieResult>>>

    fun getTrendingTv(
        page: Int = 1, timeWindow: String = "day"
    ): Flow<DataState<ListResponse<TvResult>>>

    fun getTrendingPeople(
        page: Int = 1, timeWindow: String = "day"
    ): Flow<DataState<ListResponse<PeopleResult>>>

    fun getMovieGenres(): Flow<DataState<GenreResponse>>

    fun getTvGenres(): Flow<DataState<GenreResponse>>

    fun getMovieList(
        movieListType: String,
        page: Int = 1,
    ): Flow<DataState<ListResponse<MovieResult>>>

    fun getTvList(
        tvListType: String,
        page: Int = 1,
    ): Flow<DataState<ListResponse<TvResult>>>

    fun getPopularPeopleList(
        page: Int = 1,
    ): Flow<DataState<ListResponse<PeopleResult>>>

    fun search(
        query: String,
        page: Int = 1,
    ): Flow<DataState<ListResponse<MediaResult>>>

    fun searchMovie(
        query: String,
        page: Int = 1,
    ): Flow<DataState<ListResponse<MovieResult>>>

    fun searchTv(
        query: String,
        page: Int = 1,
    ): Flow<DataState<ListResponse<TvResult>>>

    fun searchPeople(
        query: String,
        page: Int = 1,
    ): Flow<DataState<ListResponse<PeopleResult>>>

}