package com.ezhilan.cine.domain.entity

data class AppConfigData(
    val googleApiKey: String,
    val isForceUpdate: Boolean,
    val isPartialUpdate: Boolean,
    val versionCode: Int? = null,
)
