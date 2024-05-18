package com.ezhilan.cine.domain.useCases.user

import com.ezhilan.cine.R
import com.ezhilan.cine.core.Constants
import com.ezhilan.cine.data.model.remote.request.OTPVerificationRequest
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.UserVerificationStatus
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.presentation.util.UiText
import com.ezhilan.cine.presentation.util.contains
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(private val repo: AuthRepository) {

    operator fun invoke(
        phoneNumber: String, otp: String
    ): Flow<DataState<UserVerificationStatus>> = when {
        otp.isEmpty() -> flowOf(DataState.Error.Local(UiText.Resource(R.string.error_otp_empty)))

        otp.toIntOrNull() == null || otp.length != 4 -> flowOf(
            DataState.Error.Local(
                UiText.Resource(
                    R.string.error_otp_invalid
                )
            )
        )

        else -> repo.verifyOTP(
            OTPVerificationRequest(phone = phoneNumber.toLong(), otp = otp.toInt())
        ).map { dataState ->
            when (dataState) {
                is DataState.InProgress -> dataState

                is DataState.Success -> {
                    repo.storeUserData(dataState.data)
                    try {
                        repo.login()
                        DataState.Success(UserVerificationStatus.Done)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        DataState.Error.Local()
                    }
                }

                is DataState.Error -> {
                    if (dataState is DataState.Error.Remote && dataState.message is UiText.Value && (dataState.message.contains(
                            Constants.KEY_INCORRECT
                        ) || dataState.message.contains(Constants.KEY_INVALID))
                    ) {
                        DataState.Error.Local(dataState.message)
                    } else {
                        dataState
                    }
                }
            }
        }
    }
}