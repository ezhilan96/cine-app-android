package com.ezhilan.cine.domain.useCases.core

import com.ezhilan.cine.core.di.IoDispatcher
import com.ezhilan.cine.data.model.remote.response.AllTrendingListDetailResponse
import com.ezhilan.cine.data.model.remote.response.Genre
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.entity.TrendingData
import com.ezhilan.cine.domain.useCases.home.GetGenresUseCase
import com.ezhilan.cine.presentation.util.AppTextActions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapEntityUseCase @Inject constructor(
    private val getGenres: GetGenresUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    lateinit var genres: List<Genre>
    val appTextActions = AppTextActions()

    init {
        CoroutineScope(ioDispatcher).launch {
            getGenres().collect {
                if (it is DataState.Success) {
                    genres = it.data
                }
            }
        }
    }

    suspend operator fun invoke(response: AllTrendingListDetailResponse): TrendingData? {

        return try {

            val mediaType = try {
                MediaType.valueOf(response.media_type!!)
            } catch (e: Exception) {
                MediaType.all
            }
            TrendingData(
                id = response.id?.toString()!!,
                mediaType = mediaType,
                title = if (response.title.isNullOrEmpty()) {
                    response.name ?: "-"
                } else {
                    response.title
                },
                overview = response.overview ?: "-",
                backdropPath = response.backdrop_path?.ifEmpty { null },
                posterPath = response.poster_path?.ifEmpty { null },
                releaseYear = when (mediaType) {
                    MediaType.movie -> response.release_date?.split("-")
                        ?.first() ?: "-"
                    MediaType.tv -> response.first_air_date?.split("-")
                        ?.first() ?: "-"
                    else -> "-"
                },
                genres = response.genre_ids?.map { id -> genres.find { it.id == id }!! }
                    ?: listOf(),
                rating = if (response.vote_count == 0) {
                    "-"
                } else {
                    appTextActions.limitDecimalPoint(response.vote_average)
                },
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}