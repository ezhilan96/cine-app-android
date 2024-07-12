package com.ezhilan.cine.data.model.remote.response.core

data class ListResponse<T>(
    val page: Int?,
    val results: List<T?>?,
    val total_pages: Int?,
    val total_results: Int?
)