package com.ezhilan.cine.data.dataSource.remote

import com.ezhilan.cine.data.model.remote.request.GetOtpRequest
import com.ezhilan.cine.data.model.remote.request.OTPVerificationRequest
import com.ezhilan.cine.data.model.remote.response.OTPVerificationResponse
import com.ezhilan.cine.data.model.remote.response.PhoneVerificationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/v2/drivers/get_otp")
    suspend fun getOtp(@Body getOtpRequest: GetOtpRequest): PhoneVerificationResponse

    @POST("/v2/drivers/verify_otp")
    suspend fun verifyOTP(@Body otpVerificationRequest: OTPVerificationRequest): OTPVerificationResponse
}

