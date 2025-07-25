package com.ezhilan.cine.data.model.remote.response.home

data class MediaResult(
    val adult: Boolean?,
    val backdrop_path: String?,
    val first_air_date: String?,
    val gender: Int?,
    val genre_ids: List<Int?>?,
    val id: Int?,
    val known_for_department: String?,
    val media_type: String?,
    val name: String?,
    val origin_country: List<String?>?,
    val original_language: String?,
    val original_name: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val profile_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?
)