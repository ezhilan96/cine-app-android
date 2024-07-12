package com.ezhilan.cine.domain.useCases.core

import com.ezhilan.cine.data.model.remote.response.home.Genre
import com.ezhilan.cine.data.model.remote.response.home.MediaResult
import com.ezhilan.cine.data.model.remote.response.home.MovieResult
import com.ezhilan.cine.data.model.remote.response.home.PeopleResult
import com.ezhilan.cine.data.model.remote.response.home.TvResult
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.MediaData
import com.ezhilan.cine.domain.entity.MediaType
import com.ezhilan.cine.domain.useCases.home.GetGenresUseCase
import com.ezhilan.cine.presentation.util.AppTextActions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MapEntityUseCase @Inject constructor(
    private val getGenres: GetGenresUseCase,
) {
    private val appTextActions = AppTextActions()
    private var genres: List<Genre> = listOf()

    suspend operator fun invoke(response: MediaResult): Flow<MediaData> = flow {
        if (genres.isEmpty()) {
            genres = (getGenres().filter { it is DataState.Success }
                .first() as? DataState.Success<List<Genre>>)?.data ?: listOf()
        }
        val result = try {
            val mediaType = try {
                MediaType.valueOf(response.media_type!!)
            } catch (e: Exception) {
                MediaType.all
            }
            MediaData(
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
                profilePath = response.profile_path?.ifEmpty { null },
                peopleType = when (response.known_for_department) {
                    "Acting" -> {
                        when (response.gender) {
                            1 -> "Actress"
                            2 -> "Actor"
                            else -> "Actor/Actress"
                        }
                    }

                    "Directing" -> {
                        "Director"
                    }

                    "Sound" -> {
                        "Music composer"
                    }

                    "Art" -> {
                        "Artist"
                    }

                    "Camera" -> {
                        "Cinematographer"
                    }

                    else -> {
                        ""
                    }
                },
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
        emit(result)
    }.filterNotNull()

    suspend operator fun invoke(response: MovieResult): Flow<MediaData> = flow {
        if (genres.isEmpty()) {
            genres = (getGenres().filter { it is DataState.Success }
                .first() as? DataState.Success<List<Genre>>)?.data ?: listOf()
        }
        val result = try {
            MediaData(
                id = response.id?.toString()!!,
                mediaType = MediaType.movie,
                title = response.title ?: "-",
                overview = response.overview ?: "-",
                backdropPath = response.backdrop_path?.ifEmpty { null },
                posterPath = response.poster_path?.ifEmpty { null },
                releaseYear = response.release_date?.split("-")?.first() ?: "-",
                genres = response.genre_ids?.mapNotNull { id -> genres.find { it.id == id } }
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
        emit(result)
    }.filterNotNull()

    suspend operator fun invoke(response: TvResult): Flow<MediaData> = flow {
        if (genres.isEmpty()) {
            genres = (getGenres().filter { it is DataState.Success }
                .first() as? DataState.Success<List<Genre>>)?.data ?: listOf()
        }
        val result = try {
            MediaData(
                id = response.id?.toString()!!,
                mediaType = MediaType.tv,
                title = response.name ?: "-",
                overview = response.overview ?: "-",
                backdropPath = response.backdrop_path?.ifEmpty { null },
                posterPath = response.poster_path?.ifEmpty { null },
                releaseYear = response.first_air_date?.split("-")?.first() ?: "-",
                genres = response.genre_ids?.mapNotNull { id -> genres.find { it.id == id } }
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
        emit(result)
    }.filterNotNull()

    suspend operator fun invoke(response: PeopleResult): Flow<MediaData> = flow {
        val result = try {
            MediaData(
                id = response.id?.toString()!!,
                mediaType = MediaType.person,
                title = response.name ?: "-",
                profilePath = response.profile_path?.ifEmpty { null },
                peopleType = when (response.known_for_department) {
                    "Acting" -> {
                        when (response.gender) {
                            1 -> "Actress"
                            2 -> "Actor"
                            else -> "Actor/Actress"
                        }
                    }

                    "Directing" -> {
                        "Director"
                    }

                    "Sound" -> {
                        "Music composer"
                    }

                    "Art" -> {
                        "Artist"
                    }

                    "Camera" -> {
                        "Cinematographer"
                    }

                    "Writing" -> {
                        "Writer"
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
        emit(result)
    }.filterNotNull()
}