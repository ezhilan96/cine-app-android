package com.ezhilan.cine.domain.entity

import com.ezhilan.cine.data.model.remote.response.home.Genre

enum class MediaType { movie, tv, person, all }

data class MediaData(
    val id: String,
    val title: String,
    val overview: String? = null,
    val backdropPath: String? = null,
    val posterPath: String? = null,
    val profilePath: String? = null,
    val releaseYear: String? = null,
    val mediaType: MediaType,
    val genres: List<Genre>? = null,
    val rating: String? = null,
    val peopleType: String? = null,
)
