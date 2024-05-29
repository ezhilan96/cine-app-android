package com.ezhilan.cine.domain.useCases.home

import com.ezhilan.cine.data.model.remote.response.home.MediaResult
import com.ezhilan.cine.data.model.remote.response.home.MovieResult
import com.ezhilan.cine.data.model.remote.response.home.PeopleResult
import com.ezhilan.cine.data.model.remote.response.home.TvResult
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.domain.useCases.core.MapEntityUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTrendingListUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    private var mediaType: MediaType = MediaType.all
    private var currentPage: Int = 0
    private var list: MutableList<MediaData> = mutableListOf()
    operator fun invoke(
        mediaType: MediaType,
        pagingEnabled: Boolean = false,
        timeWindow: String = "day",
    ): Flow<DataState<List<MediaData>>> {
        if (mediaType != this.mediaType || !pagingEnabled) {
            this.mediaType = mediaType
            currentPage = 0
            list.clear()
        }
        return when (mediaType) {
            MediaType.all -> repo.getAllTrending(
                page = currentPage + 1,
                timeWindow = timeWindow,
            )

            MediaType.movie -> repo.getTrendingMovies(
                page = currentPage + 1,
                timeWindow = timeWindow,
            )

            MediaType.tv -> repo.getTrendingTv(
                page = currentPage + 1,
                timeWindow = timeWindow,
            )

            MediaType.person -> repo.getTrendingPeople(
                page = currentPage + 1,
                timeWindow = timeWindow,
            )
        }.map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState
                is DataState.Success -> {
                    currentPage = dataState.data.page ?: 0
                    dataState.data.results?.mapNotNull {
                        it?.let {
                            when (it) {
                                is MediaResult -> mapEntity(it).first()
                                is MovieResult -> mapEntity(it).first()
                                is TvResult -> mapEntity(it).first()
                                is PeopleResult -> mapEntity(it).first()
                                else -> null
                            }
                        }
                    }?.let { list.addAll(it) }
                    DataState.Success(list)
                }

                is DataState.Error -> if (currentPage > 0) {
                    invoke(
                        pagingEnabled = false,
                        timeWindow = timeWindow,
                        mediaType = mediaType,
                    ).filter { it !is DataState.InProgress }.first()
                } else {
                    dataState
                }
            }
        }
    }
}