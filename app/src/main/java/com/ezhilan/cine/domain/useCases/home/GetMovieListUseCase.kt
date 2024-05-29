package com.ezhilan.cine.domain.useCases.home

import android.util.Log
import com.ezhilan.cine.core.TAG
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
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    private var list: MutableList<MediaData> = mutableListOf()
    private var currentPage: Int = 0
    private var movieListType: MovieListType = MovieListType.now_playing

    operator fun invoke(
        movieListType: MovieListType,
        pagingEnabled: Boolean = false,
    ): Flow<DataState<List<MediaData>>> {
        if (movieListType != this.movieListType || !pagingEnabled) {
            list.clear()
            this.movieListType = movieListType
            currentPage = 0
        }
        return repo.getMovieList(
            movieListType = movieListType.toString(),
            page = currentPage + 1,
        ).map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState
                is DataState.Success -> {
                    currentPage = dataState.data.page ?: 0
                    dataState.data.results?.mapNotNull { it?.let { mapEntity(it).first() } }
                        ?.let { list.addAll(it) }
                    Log.i(TAG, "invoke: ${list.map { it.title.split(" ").first().first() }}")
                    DataState.Success(list)
                }

                is DataState.Error -> if (currentPage > 0) {
                    invoke(
                        movieListType = movieListType,
                        pagingEnabled = false,
                    ).filter { it !is DataState.InProgress }.first()
                } else {
                    dataState
                }
            }
        }
    }
}