package com.ezhilan.cine.data.repository

import com.ezhilan.cine.core.di.IoDispatcher
import com.ezhilan.cine.data.dataSource.local.dataStore.UserPreferencesDataStore
import com.ezhilan.cine.data.dataSource.remote.HomeService
import com.ezhilan.cine.data.model.remote.response.AllTrendingListResponse
import com.ezhilan.cine.data.model.remote.response.Genre
import com.ezhilan.cine.data.model.remote.response.GenreResponse
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
        timeWindow: String,
        language: String,
    ): Flow<DataState<AllTrendingListResponse>> = executeRemoteCall {
        homeService.getAllTrending(timeWindow, language)
    }

    override fun getTrendingMovies(
        timeWindow: String,
        language: String,
    ): Flow<DataState<AllTrendingListResponse>> = executeRemoteCall {
        homeService.getTrendingMovies(timeWindow, language)
    }

    override fun getTrendingTv(
        timeWindow: String,
        language: String,
    ): Flow<DataState<AllTrendingListResponse>> = executeRemoteCall {
        homeService.getTrendingTv(timeWindow, language)
    }

    override fun getMovieGenres(): Flow<DataState<GenreResponse>> = executeRemoteCall {
        homeService.getMovieGeneres()
    }

    override fun getTvGenres(): Flow<DataState<GenreResponse>> = executeRemoteCall {
        homeService.getTvGeneres()
    }

}