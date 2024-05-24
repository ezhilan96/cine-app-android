package com.ezhilan.cine.data.repository

import com.ezhilan.cine.core.di.IoDispatcher
import com.ezhilan.cine.data.dataSource.local.dataStore.UserPreferencesDataStore
import com.ezhilan.cine.data.dataSource.remote.HomeService
import com.ezhilan.cine.data.model.remote.response.Genre
import com.ezhilan.cine.data.model.remote.response.GenreResponse
import com.ezhilan.cine.data.model.remote.response.core.ListResponse
import com.ezhilan.cine.data.model.remote.response.trending.AllTrendingResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingMovieResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingPeopleResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingTvResult
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.domain.repository.core.NetworkConnectionRepository
import com.ezhilan.cine.domain.repository.core.RemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    connectionRepo: NetworkConnectionRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val authRepo: AuthRepository,
    private val homeService: HomeService,
    private val dataStore: UserPreferencesDataStore,
) : HomeRepository, RemoteRepository(connectionRepo) {

    private val _genres: MutableList<Genre> = mutableListOf()
    override val genres: List<Genre>
        get() = _genres

    override fun handleUnAuthorized() {
        authRepo.logout()
    }

    override fun getAllTrending(
        timeWindow: String
    ): Flow<DataState<ListResponse<AllTrendingResult>>> = executeRemoteCall {
        homeService.getAllTrending(timeWindow)
    }

    override fun getTrendingMovies(
        timeWindow: String
    ): Flow<DataState<ListResponse<TrendingMovieResult>>> = executeRemoteCall {
        homeService.getTrendingMovies(timeWindow)
    }

    override fun getTrendingTv(
        timeWindow: String
    ): Flow<DataState<ListResponse<TrendingTvResult>>> = executeRemoteCall {
        homeService.getTrendingTv(timeWindow)
    }

    override fun getTrendingPeople(
        timeWindow: String
    ): Flow<DataState<ListResponse<TrendingPeopleResult>>> = executeRemoteCall {
        homeService.getTrendingPeople(timeWindow)
    }

    override fun getMovieGenres(): Flow<DataState<GenreResponse>> = executeRemoteCall {
        homeService.getMovieGenres()
    }

    override fun getTvGenres(): Flow<DataState<GenreResponse>> = executeRemoteCall {
        homeService.getTvGenres()
    }

}