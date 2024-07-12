package com.ezhilan.cine.data.repository

import com.ezhilan.cine.data.dataSource.local.dataStore.UserPreferencesDataStore
import com.ezhilan.cine.data.dataSource.remote.HomeService
import com.ezhilan.cine.data.model.remote.response.core.ListResponse
import com.ezhilan.cine.data.model.remote.response.home.Genre
import com.ezhilan.cine.data.model.remote.response.home.GenreResponse
import com.ezhilan.cine.data.model.remote.response.home.MediaResult
import com.ezhilan.cine.data.model.remote.response.home.MovieResult
import com.ezhilan.cine.data.model.remote.response.home.PeopleResult
import com.ezhilan.cine.data.model.remote.response.home.TvResult
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.domain.repository.core.NetworkConnectionRepository
import com.ezhilan.cine.domain.repository.core.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    connectionRepo: NetworkConnectionRepository,
    private val authRepo: AuthRepository,
    private val homeService: HomeService,
    private val dataStore: UserPreferencesDataStore,
) : HomeRepository, RemoteRepository(connectionRepo) {

    private val _genres: MutableList<Genre> = mutableListOf()
    override val genres: List<Genre>
        get() = _genres

    override fun setGenres(genres: List<Genre>) {
        _genres.clear()
        _genres.addAll(genres)
    }

    override fun handleUnAuthorized() {
        authRepo.logout()
    }

    override fun getAllTrending(
        page: Int, timeWindow: String
    ): Flow<DataState<ListResponse<MediaResult>>> = executeRemoteCall {
        homeService.getAllTrending(
            time = timeWindow,
            page = page,
        )
    }

    override fun getTrendingMovies(
        page: Int, timeWindow: String
    ): Flow<DataState<ListResponse<MovieResult>>> = executeRemoteCall {
        homeService.getTrendingMovies(
            time = timeWindow,
            page = page,
        )
    }

    override fun getTrendingTv(
        page: Int, timeWindow: String
    ): Flow<DataState<ListResponse<TvResult>>> = executeRemoteCall {
        homeService.getTrendingTv(
            time = timeWindow,
            page = page,
        )
    }

    override fun getTrendingPeople(
        page: Int, timeWindow: String
    ): Flow<DataState<ListResponse<PeopleResult>>> = executeRemoteCall {
        homeService.getTrendingPeople(
            time = timeWindow,
            page = page,
        )
    }

    override fun getMovieGenres(): Flow<DataState<GenreResponse>> = executeRemoteCall {
        homeService.getMovieGenres()
    }

    override fun getTvGenres(): Flow<DataState<GenreResponse>> = executeRemoteCall {
        homeService.getTvGenres()
    }

    override fun getMovieList(
        movieListType: String,
        page: Int,
    ): Flow<DataState<ListResponse<MovieResult>>> = executeRemoteCall {
        homeService.getMovieList(
            movieListType = movieListType,
            page = page,
            region = dataStore.region.first(),
        )
    }

    override fun getTvList(tvListType: String, page: Int): Flow<DataState<ListResponse<TvResult>>> =
        executeRemoteCall {
            homeService.getTvList(
                tvListType = tvListType,
                page = page,
            )
        }

    override fun getPopularPeopleList(page: Int): Flow<DataState<ListResponse<PeopleResult>>> =
        executeRemoteCall {
            homeService.getPopularPeople(page = page)
        }

}