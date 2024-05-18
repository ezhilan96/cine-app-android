package com.ezhilan.cine.domain.useCases.core

import com.ezhilan.cine.data.model.remote.request.DeviceDataSubmitRequest
import com.ezhilan.cine.domain.repository.HomeRepository
import javax.inject.Inject

class SubmitDeviceDataUseCase @Inject constructor(private val repo: HomeRepository) {
    operator fun invoke(token: String) =
        repo.submitDeviceData(DeviceDataSubmitRequest(deviceId = token))
}