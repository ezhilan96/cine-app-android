package com.ezhilan.cine.data.model.remote.response

data class PhoneVerificationResponse(
    val `data`: Data?,
    val message: String?,
)

data class Data(
    val countryCode: String?,
    val phone: Long?,
)

data class OTPVerificationResponse(
    val id: Int,
    val token: String,
    val name: String?,
    val phone: Long?,
)