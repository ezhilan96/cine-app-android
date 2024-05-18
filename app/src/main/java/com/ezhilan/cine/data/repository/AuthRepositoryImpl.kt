package com.ezhilan.cine.data.repository

import com.ezhilan.cine.core.di.IoDispatcher
import com.ezhilan.cine.data.dataSource.local.dataStore.UserPreferencesDataStore
import com.ezhilan.cine.data.dataSource.remote.AuthService
import com.ezhilan.cine.data.dataSource.remote.HomeService
import com.ezhilan.cine.data.model.remote.request.GetOtpRequest
import com.ezhilan.cine.data.model.remote.request.OTPVerificationRequest
import com.ezhilan.cine.data.model.remote.response.Data
import com.ezhilan.cine.data.model.remote.response.OTPVerificationResponse
import com.ezhilan.cine.data.model.remote.response.PhoneVerificationResponse
import com.ezhilan.cine.data.util.DataState
import com.ezhilan.cine.domain.entity.UserData
import com.ezhilan.cine.domain.repository.AuthRepository
import com.ezhilan.cine.domain.repository.core.NetworkConnectionRepository
import com.ezhilan.cine.domain.repository.core.RemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    connectionRepo: NetworkConnectionRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val authService: AuthService,
    private val homeService: HomeService,
    private val dataStore: UserPreferencesDataStore,
) : AuthRepository, RemoteRepository(connectionRepo) {

    override fun handleUnAuthorized() {
        logout().launchIn(CoroutineScope(ioDispatcher))
    }

    override val isLoggedIn: Flow<Boolean>
        get() = dataStore.isLoggedIn

    override val userDetails: Flow<UserData?> = dataStore.userDetails

    private val _autoReadOtp: MutableStateFlow<String?> = MutableStateFlow(null)
    override val autoReadOtp: Flow<String?>
        get() = _autoReadOtp

    override fun updateAutoReadOtp(otp: String?) {
        _autoReadOtp.update { otp }
    }

    private val _otpCountDownTime: MutableStateFlow<Int> = MutableStateFlow(0)
    override val otpCountDownInSec: StateFlow<Int>
        get() = _otpCountDownTime

    override fun updateOtpCountDownTime(sec: Int) {
        _otpCountDownTime.value = sec
    }

    override fun sendOtp(getOtpRequest: GetOtpRequest): Flow<DataState<PhoneVerificationResponse>> =
        if (getOtpRequest.phone == 1234567890L) {
            flowOf(
                DataState.Success(
                    PhoneVerificationResponse(
                        data = Data(
                            countryCode = "+91", 1234567890
                        ), message = "OTP sedn successfully"
                    )
                )
            )
        } else executeRemoteCall {
            authService.getOtp(getOtpRequest)
        }

    override fun verifyOTP(otpVerificationRequest: OTPVerificationRequest): Flow<DataState<OTPVerificationResponse>> =
        if (otpVerificationRequest.phone == 1234567890L) {
            flowOf(
                DataState.Success(
                    OTPVerificationResponse(
                        id = 1,
                        token = "token",
                        name = "Username",
                        phone = 1234567890,
                    )
                )
            )
        } else executeRemoteCall {
            authService.verifyOTP(otpVerificationRequest)
        }

    override suspend fun storeUserData(otpVerificationResponse: OTPVerificationResponse): Unit =
        dataStore.storeUserData(otpVerificationResponse)

    override suspend fun login(): Unit = dataStore.login()

    override fun logout(): Flow<DataState<Response<Unit>>> = executeRemoteCall {
        homeService.logout()
    }
}