package com.ezhilan.cine.domain.useCases.home

import com.ezhilan.cine.data.model.remote.response.Genre
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetGenresUseCase @Inject constructor(private val repo: HomeRepository) {
    private val genres: MutableList<Genre?> = mutableListOf()

    operator fun invoke(): Flow<DataState<List<Genre>>> = if (repo.genres.isNotEmpty()) {
        flowOf(DataState.Success(repo.genres))
    } else {
        flow {
            repo.getMovieGenres().collect { movieGenreDataState ->
                when (movieGenreDataState) {
                    is DataState.Success -> movieGenreDataState.data.genres?.let { genres.addAll(it) }

                    is DataState.Error -> emit(movieGenreDataState)

                    is DataState.InProgress -> emit(movieGenreDataState)
                }

                if (movieGenreDataState !is DataState.InProgress) {
                    repo.getTvGenres().collect { tvGenreDataState ->
                        when (tvGenreDataState) {
                            is DataState.Success -> {
                                tvGenreDataState.data.genres?.let { genres.addAll(it) }
                                repo.setGenres(genres.filterNotNull())
                                emit(DataState.Success(genres.filterNotNull()))
                            }

                            is DataState.Error -> emit(tvGenreDataState)

                            is DataState.InProgress -> emit(tvGenreDataState)
                        }
                    }
                }
            }
        }
    }
}