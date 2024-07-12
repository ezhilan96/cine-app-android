package com.ezhilan.cine.domain.useCases.home.trendingList

import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.entity.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrendingListUseCase @Inject constructor(
    private val getAllTrendingList: GetAllTrendingListUseCase,
    private val getTrendingMovies: GetTrendingMoviesUseCase,
    private val getTrendingTvShows: GetTrendingTvShowsUseCase,
    private val getTrendingPeople: GetTrendingPeopleUseCase,
) {
    operator fun invoke(
        mediaType: MediaType,
        pagingEnabled: Boolean = false,
        timeWindow: String,
    ): Flow<DataState<List<MediaData>>> = when (mediaType) {
        MediaType.all -> getAllTrendingList(pagingEnabled, timeWindow)
        MediaType.movie -> getTrendingMovies(pagingEnabled, timeWindow)
        MediaType.tv -> getTrendingTvShows(pagingEnabled, timeWindow)
        MediaType.person -> getTrendingPeople(pagingEnabled, timeWindow)
    }
}