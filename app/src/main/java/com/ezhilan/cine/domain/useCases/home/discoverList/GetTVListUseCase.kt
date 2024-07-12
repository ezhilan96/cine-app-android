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

enum class TvListType { airing_today, on_the_air, popular, top_rated }

class GetTVListUseCase @Inject constructor(
    private val getTvShowsAiringTodayList: GetTVShowsAiringTodayListUseCase,
    private val getTvShowsOnTheAirList: GetTVShowsOnTheAirListUseCase,
    private val getPopularTVShowsList: GetPopularTVShowsListUseCase,
    private val getTopRatedTVShowsList: GetTopRatedTVShowsListUseCase,
) {
    operator fun invoke(
        tvListType: TvListType,
        pagingEnabled: Boolean = false,
    ): Flow<DataState<List<MediaData>>> =
        when (tvListType) {
            TvListType.airing_today -> getTvShowsAiringTodayList(pagingEnabled)
            TvListType.on_the_air -> getTvShowsOnTheAirList(pagingEnabled)
            TvListType.popular -> getPopularTVShowsList(pagingEnabled)
            TvListType.top_rated -> getTopRatedTVShowsList(pagingEnabled)
        }
}

class GetTVShowsAiringTodayListUseCase @Inject constructor(
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
        return repo.getTvList(
            tvListType = TvListType.airing_today.toString(),
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

class GetTVShowsOnTheAirListUseCase @Inject constructor(
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
        return repo.getTvList(
            tvListType = TvListType.on_the_air.toString(),
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

class GetPopularTVShowsListUseCase @Inject constructor(
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
        return repo.getTvList(
            tvListType = TvListType.popular.toString(),
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

class GetTopRatedTVShowsListUseCase @Inject constructor(
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
        return repo.getTvList(
            tvListType = TvListType.top_rated.toString(),
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