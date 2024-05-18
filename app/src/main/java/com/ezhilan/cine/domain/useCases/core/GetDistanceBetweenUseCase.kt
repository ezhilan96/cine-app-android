package com.ezhilan.cine.domain.useCases.core

import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.LatLng
import com.ezhilan.cine.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDistanceBetweenUseCase @Inject constructor(private val driverRepo: HomeRepository) {

    suspend operator fun invoke(
        origin: LatLng,
        destination: LatLng,
    ): Flow<DataState<Float>> {
        return driverRepo.getDirection(origin, destination).map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState

                is DataState.Success -> {
                    val distance =
                        (dataState.data.routes?.firstOrNull()?.legs?.firstOrNull()?.distance?.value
                            ?: 0) / 1000.0f
                    DataState.Success(distance)
                }

                is DataState.Error -> dataState
            }
        }
    }
}