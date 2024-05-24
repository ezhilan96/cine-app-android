package com.ezhilan.cine.domain.entity

import com.ezhilan.cine.data.model.remote.response.Genre

enum class MediaType { tv, movie, all }

data class TrendingData(
    val id: String,
    val title: String,
    val overview: String,
    val backdropPath: String?,
    val posterPath: String?,
    val releaseYear: String,
    val mediaType: MediaType,
    val genres: List<Genre>,
    val rating: String,
)
