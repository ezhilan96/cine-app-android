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

class SearchUseCase @Inject constructor(
    private val repo: HomeRepository,
    private val mapEntity: MapEntityUseCase,
) {
    private var currentPage: Int = 0
    private var list: MutableList<MediaData> = mutableListOf()
    operator fun invoke(
        query: String,
        mediaType: MediaType = MediaType.all,
        pagingEnabled: Boolean = false,
    ): Flow<DataState<List<MediaData>>> {
        if (!pagingEnabled) {
            currentPage = 0
            list.clear()
        }
        return when (mediaType) {
            MediaType.all -> repo.search(
                query = query,
                page = currentPage + 1,
            )

            MediaType.movie -> repo.searchMovie(
                query = query,
                page = currentPage + 1,
            )

            MediaType.tv -> repo.searchTv(
                query = query,
                page = currentPage + 1,
            )

            MediaType.person -> repo.searchPeople(
                query = query,
                page = currentPage + 1,
            )
        }.map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState
                is DataState.Success -> {
                    currentPage = dataState.data.page ?: 0
                    dataState.data.results?.mapNotNull {
                        it?.let {
                            when (it) {
                                is MovieResult -> mapEntity(it).first()
                                is TvResult -> mapEntity(it).first()
                                is PeopleResult -> mapEntity(it).first()
                                else -> mapEntity(it as MediaResult).first()
                            }
                        }
                    }
                        ?.let { list.addAll(it) }
                    DataState.Success(list)
                }

                is DataState.Error -> if (currentPage > 0) {
                    invoke(
                        query = query,
                        pagingEnabled = false,
                    ).filter { it !is DataState.InProgress }.first()
                } else {
                    dataState
                }
            }
        }
    }
}