package com.ezhilan.cine.presentation.components

import android.os.CountDownTimer
import com.ezhilan.cine.core.Constants
import com.ezhilan.cine.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OtpCountDownTimer @Inject constructor(private val authRepo: AuthRepository) : CountDownTimer(
    Constants.SIXTY_SEC_IN_MILLIS + Constants.ONE_SEC_IN_MILLIS,
    Constants.ONE_SEC_IN_MILLIS,
) {
    override fun onTick(millisUntilFinished: Long) {
        authRepo.updateOtpCountDownTime((millisUntilFinished / 1000).toInt())
    }

    override fun onFinish() {
        authRepo.updateOtpCountDownTime(0)
    }
}