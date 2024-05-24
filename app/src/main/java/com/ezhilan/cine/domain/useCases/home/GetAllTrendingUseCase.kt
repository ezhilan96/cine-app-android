package com.ezhilan.cine.domain.useCases.home

import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.TrendingData
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.domain.useCases.core.MapEntityUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllTrendingUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    operator fun invoke(
        timeWindow: String = "day",
        language: String = "en-US",
        mediaType: MediaType = MediaType.all,
    ): Flow<DataState<List<TrendingData>>> =
        when (mediaType) {
            MediaType.movie -> repo.getTrendingMovies(timeWindow, language)
            MediaType.tv -> repo.getTrendingTv(timeWindow, language)
            MediaType.all -> repo.getAllTrending(timeWindow, language)
        }.map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState
                is DataState.Success -> {
                    val list =
                        dataState.data.results?.mapNotNull { it?.let { mapEntity(it) } }
                            ?: listOf()
                    DataState.Success(list)
                }

                is DataState.Error -> dataState
            }
        }
}