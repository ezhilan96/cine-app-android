package com.ezhilan.cine.domain.useCases.user

import com.ezhilan.cine.R
import com.ezhilan.cine.data.model.remote.response.OTPVerificationResponse
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.presentation.util.UiText
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class VerifyOtpTest {

    private lateinit var verifyOtp: VerifyOtpUseCase
    private val mockAuthRepository = mockk<AuthRepository>(relaxed = true)

    @Before
    fun setUp() {
        verifyOtp = VerifyOtpUseCase(mockAuthRepository)
    }

    @Test
    fun `verifyOTP - invalid otp - failure result`() {
        runTest {
            val result = verifyOtp(otp = "fhsh", phoneNumber = "12345678890").first()
            assertThat(((result as DataState.Error.Local).message as UiText.Resource).id).isEqualTo(
                R.string.error_otp_invalid
            )
        }
    }

    @Test
    fun `verifyOTP - empty otp - failure result`() {
        runTest {
            val result = verifyOtp(otp = "", phoneNumber = "1234567890").first()
            assertThat(((result as DataState.Error.Local).message as UiText.Resource).id).isEqualTo(
                R.string.error_otp_empty
            )
        }
    }

    @Test
    fun `verifyOTP - valid otp - remote success result`() {
        val phoneNumber = 9498048698
        val successDataState = DataState.Success(
            OTPVerificationResponse(
                id = 0,
                token = "",
                name = "",
                phone = 9498048698,
            )
        )
        coEvery { mockAuthRepository.verifyOTP(any()) } returns flowOf(successDataState)
        runTest {
            val result = verifyOtp(otp = "1323", phoneNumber = phoneNumber.toString()).first()
            assertThat(result).isInstanceOf(DataState.Success::class.java)
            assertThat((result as DataState.Success).data).isNotNull()
        }
    }

    @Test
    fun `verifyOTP - valid otp - local error result`() {
        val phoneNumber = 9498048698
        val errorDataState = DataState.Error.Remote(message = UiText.Value("invalid"))
        coEvery { mockAuthRepository.verifyOTP(any()) } returns flowOf(errorDataState)
        runTest {
            val result = verifyOtp(otp = "1323", phoneNumber = phoneNumber.toString()).first()
            assertThat(result).isInstanceOf(DataState.Error.Local::class.java)
        }
    }
}