package com.ezhilan.cine.domain.useCases.home.trendingList

import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.domain.useCases.core.MapEntityUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTrendingPeopleUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    private var currentPage: Int = 0
    private var list: MutableList<MediaData> = mutableListOf()
    operator fun invoke(
        pagingEnabled: Boolean = false,
        timeWindow: String = "day",
    ): Flow<DataState<List<MediaData>>> {
        if (!pagingEnabled) {
            currentPage = 0
            list.clear()
        }
        return repo.getTrendingPeople(
            page = currentPage + 1,
            timeWindow = timeWindow,
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
                    invoke(
                        pagingEnabled = false,
                        timeWindow = timeWindow,
                    ).filter { it !is DataState.InProgress }.first()
                } else {
                    dataState
                }
            }
        }
    }
}