package com.ezhilan.cine.data.model.remote.request

import com.ezhilan.cine.core.Constants

data class DeviceDataSubmitRequest(
    val deviceType: String = Constants.JSON_ANDROID,
    val deviceId: String? = null,
)