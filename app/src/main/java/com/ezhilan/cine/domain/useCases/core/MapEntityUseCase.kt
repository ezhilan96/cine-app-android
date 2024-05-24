package com.ezhilan.cine.domain.useCases.core

import com.ezhilan.cine.core.di.IoDispatcher
import com.ezhilan.cine.data.model.remote.response.Genre
import com.ezhilan.cine.data.model.remote.response.trending.AllTrendingResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingMovieResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingPeopleResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingResult
import com.ezhilan.cine.data.model.remote.response.trending.TrendingTvResult
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
    private val appTextActions = AppTextActions()

    init {
        CoroutineScope(ioDispatcher).launch {
            getGenres().collect {
                if (it is DataState.Success) {
                    genres = it.data
                }
            }
        }
    }

    operator fun invoke(response: TrendingResult): TrendingData? {

        return when (response) {
            is AllTrendingResult -> {
                try {

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
                            MediaType.movie -> response.release_date?.split("-")?.first() ?: "-"

                            MediaType.tv -> response.first_air_date?.split("-")?.first() ?: "-"

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

            is TrendingMovieResult -> {
                try {

                    val mediaType = try {
                        MediaType.valueOf(response.media_type!!)
                    } catch (e: Exception) {
                        MediaType.all
                    }
                    TrendingData(
                        id = response.id?.toString()!!,
                        mediaType = mediaType,
                        title = response.title ?: "-",
                        overview = response.overview ?: "-",
                        backdropPath = response.backdrop_path?.ifEmpty { null },
                        posterPath = response.poster_path?.ifEmpty { null },
                        releaseYear = response.release_date?.split("-")?.first() ?: "-",
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

            is TrendingTvResult -> {
                try {

                    val mediaType = try {
                        MediaType.valueOf(response.media_type!!)
                    } catch (e: Exception) {
                        MediaType.all
                    }
                    TrendingData(
                        id = response.id?.toString()!!,
                        mediaType = mediaType,
                        title = response.name ?: "-",
                        overview = response.overview ?: "-",
                        backdropPath = response.backdrop_path?.ifEmpty { null },
                        posterPath = response.poster_path?.ifEmpty { null },
                        releaseYear = response.first_air_date?.split("-")?.first() ?: "-",
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

            is TrendingPeopleResult -> {
                try {

                    val mediaType = try {
                        MediaType.valueOf(response.media_type!!)
                    } catch (e: Exception) {
                        MediaType.all
                    }
                    TrendingData(
                        id = response.id?.toString()!!,
                        mediaType = mediaType,
                        title = response.name ?: "-",
                        profilePath = response.profile_path?.ifEmpty { null },
                        peopleType = when (response.known_for_department) {
                            "Acting" -> {
                                "Actor"
                            }

                            "Directing" -> {
                                "Director"
                            }

                            else -> {
                                ""
                            }
                        }
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            else -> TODO()
        }
    }
}