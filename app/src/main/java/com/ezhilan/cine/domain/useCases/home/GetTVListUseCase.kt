package com.ezhilan.cine.domain.useCases.home

import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.repository.HomeRepository
import com.ezhilan.cine.domain.useCases.core.MapEntityUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

enum class TvListType { airing_today, on_the_air, popular, top_rated }

class GetTVListUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    private var list: MutableList<MediaData> = mutableListOf()
    private var currentPage: Int = 0
    private var tvListType: TvListType = TvListType.airing_today


    operator fun invoke(
        tvListType: TvListType,
        pagingEnabled: Boolean = false,
    ): Flow<DataState<List<MediaData>>> {
        if (tvListType != this.tvListType || !pagingEnabled) {
            list.clear()
            this.tvListType = tvListType
            currentPage = 0
        }
        return repo.getTvList(
            tvListType = tvListType.toString(),
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
                    invoke(
                        tvListType = tvListType,
                        pagingEnabled = false,
                    ).filter { it !is DataState.InProgress }.first()
                } else {
                    dataState
                }
            }
        }
    }
}