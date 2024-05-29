package com.ezhilan.cine.data.model.remote.response.home

data class PeopleResult(
    val adult: Boolean?,
    val gender: Int?,
    val id: Int?,
    val known_for: List<MediaResult?>?,
    val known_for_department: String?,
    val media_type: String?,
    val name: String?,
    val original_name: String?,
    val popularity: Double?,
    val profile_path: String?
)