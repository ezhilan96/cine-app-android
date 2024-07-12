package com.ezhilan.cine.domain.useCases.home.discoverList

import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.domain.useCases.core.MapEntityUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

enum class MovieListType { now_playing, popular, top_rated, upcoming, }

class GetMovieListUseCase @Inject constructor(
    private val getNowPlayingMovieList: GetNowPlayingMovieListUseCase,
    private val getPopularMovieList: GetPopularMovieListUseCase,
    private val getTopRatedMovieList: GetTopRatedMovieListUseCase,
    private val getUpcomingMovieList: GetUpcomingMovieListUseCase,
) {
    operator fun invoke(
        movieListType: MovieListType,
        pagingEnabled: Boolean = false,
    ): Flow<DataState<List<MediaData>>> =
        when (movieListType) {
            MovieListType.now_playing -> getNowPlayingMovieList(pagingEnabled)
            MovieListType.popular -> getPopularMovieList(pagingEnabled)
            MovieListType.top_rated -> getTopRatedMovieList(pagingEnabled)
            MovieListType.upcoming -> getUpcomingMovieList(pagingEnabled)
        }
}

class GetNowPlayingMovieListUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    private var list: MutableList<MediaData> = mutableListOf()
    private var currentPage: Int = 0

    operator fun invoke(pagingEnabled: Boolean = false): Flow<DataState<List<MediaData>>> {
        if (!pagingEnabled) {
            list.clear()
            currentPage = 0
        }
        return repo.getMovieList(
            movieListType = MovieListType.now_playing.toString(),
            page = currentPage + 1,
        ).map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState
                is DataState.Success -> {
                    currentPage = dataState.data.page ?: 0
                    dataState.data.results?.mapNotNull { it?.let { mapEntity(it).first() } }
                        ?.let { list.addAll(it) }
                    DataState.Success(list)
                }

                is DataState.Error -> if (currentPage > 0) {
                    invoke(pagingEnabled = false).filter { it !is DataState.InProgress }.first()
                } else {
                    dataState
                }
            }
        }
    }
}

class GetPopularMovieListUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    private var list: MutableList<MediaData> = mutableListOf()
    private var currentPage: Int = 0

    operator fun invoke(pagingEnabled: Boolean = false): Flow<DataState<List<MediaData>>> {
        if (!pagingEnabled) {
            list.clear()
            currentPage = 0
        }
        return repo.getMovieList(
            movieListType = MovieListType.popular.toString(),
            page = currentPage + 1,
        ).map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState
                is DataState.Success -> {
                    currentPage = dataState.data.page ?: 0
                    dataState.data.results?.mapNotNull { it?.let { mapEntity(it).first() } }
                        ?.let { list.addAll(it) }
                    DataState.Success(list)
                }

                is DataState.Error -> if (currentPage > 0) {
                    invoke(pagingEnabled = false).filter { it !is DataState.InProgress }.first()
                } else {
                    dataState
                }
            }
        }
    }
}

class GetTopRatedMovieListUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    private var list: MutableList<MediaData> = mutableListOf()
    private var currentPage: Int = 0

    operator fun invoke(pagingEnabled: Boolean = false): Flow<DataState<List<MediaData>>> {
        if (!pagingEnabled) {
            list.clear()
            currentPage = 0
        }
        return repo.getMovieList(
            movieListType = MovieListType.top_rated.toString(),
            page = currentPage + 1,
        ).map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState
                is DataState.Success -> {
                    currentPage = dataState.data.page ?: 0
                    dataState.data.results?.mapNotNull { it?.let { mapEntity(it).first() } }
                        ?.let { list.addAll(it) }
                    DataState.Success(list)
                }

                is DataState.Error -> if (currentPage > 0) {
                    invoke(pagingEnabled = false).filter { it !is DataState.InProgress }.first()
                } else {
                    dataState
                }
            }
        }
    }
}

class GetUpcomingMovieListUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    private var list: MutableList<MediaData> = mutableListOf()
    private var currentPage: Int = 0

    operator fun invoke(pagingEnabled: Boolean = false): Flow<DataState<List<MediaData>>> {
        if (!pagingEnabled) {
            list.clear()
            currentPage = 0
        }
        return repo.getMovieList(
            movieListType = MovieListType.upcoming.toString(),
            page = currentPage + 1,
        ).map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState
                is DataState.Success -> {
                    currentPage = dataState.data.page ?: 0
                    dataState.data.results?.mapNotNull { it?.let { mapEntity(it).first() } }
                        ?.let { list.addAll(it) }
                    DataState.Success(list)
                }

                is DataState.Error -> if (currentPage > 0) {
                    invoke(pagingEnabled = false).filter { it !is DataState.InProgress }.first()
                } else {
                    dataState
                }
            }
        }
    }
}

