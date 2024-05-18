package com.ezhilan.cine.domain.repository

import android.location.Location
import com.ezhilan.cine.data.model.remote.request.DeviceDataSubmitRequest
import com.ezhilan.cine.data.model.remote.response.AppConfigResponse
import com.ezhilan.cine.data.model.remote.response.BookingListResponse
import com.ezhilan.cine.data.model.remote.response.DirectionResponse
import com.ezhilan.cine.data.model.remote.response.ImageUploadResponse
import com.ezhilan.cine.data.model.remote.response.ListResponse
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.LatLng
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response

interface HomeRepository {

    fun getAppConfig(): Flow<DataState<AppConfigResponse>>

    fun updateLocation(location: Location, deviceId: String)

    fun onLocationUpdateStop()

    fun enableTripDistanceTracking(bookingId: String)

    fun getOnGoingTripDistance(bookingId: String): Double?

    fun getBookingList(
        skip: Int,
        isPendingList: Boolean = true,
    ): Flow<DataState<ListResponse<BookingListResponse>>>

    fun submitDeviceData(deviceDataSubmitRequest: DeviceDataSubmitRequest): Flow<DataState<Response<Unit>>>

    fun uploadFile(image: MultipartBody.Part): Flow<DataState<ImageUploadResponse>>

    suspend fun getDirection(
        origin: LatLng,
        destination: LatLng,
    ): Flow<DataState<DirectionResponse>>
}