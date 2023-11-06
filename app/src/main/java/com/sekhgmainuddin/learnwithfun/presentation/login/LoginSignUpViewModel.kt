package com.sekhgmainuddin.learnwithfun.presentation.login

import android.icu.text.SimpleDateFormat
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.use_case.login_signup.GetOTPUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.login_signup.VerifyOTPUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class LoginSignUpViewModel @Inject constructor(
    private val getOTPUseCase: GetOTPUseCase,
    private val verifyOTPUseCase: VerifyOTPUseCase
) : ViewModel() {

    private var _getOTPState = MutableStateFlow<GetOTPState>(GetOTPState.Initial)
    val getOTPState: StateFlow<GetOTPState>
        get() = _getOTPState

    fun getOTP(getOTPBodyParams: GetOTPBodyParams) = viewModelScope.launch(Dispatchers.IO) {
        _getOTPState.value = GetOTPState.Loading
        getOTPUseCase(getOTPBodyParams).catch {
            _getOTPState.value = GetOTPState.Error(it.localizedMessage ?: "Some Error Occurred")
            Log.e("TAG", "getOTP: ${it.localizedMessage}")
        }.collect {
            otpTimer?.cancel()
            startOTPTimer()
            _getOTPState.value = GetOTPState.Sent
        }
    }

    private var _verifyOTPState = MutableStateFlow<VerifyOTPState>(VerifyOTPState.Initial)
    val verifyOTPState: StateFlow<VerifyOTPState>
        get() = _verifyOTPState

    fun verifyOTP(verifyOTPBodyParams: VerifyOTPBodyParams) =
        viewModelScope.launch(Dispatchers.IO) {
            otpTimer?.cancel()
            _verifyOTPState.value = VerifyOTPState.Loading
            verifyOTPUseCase(verifyOTPBodyParams).catch {
                _verifyOTPState.value =
                    VerifyOTPState.Error(it.localizedMessage ?: "Some Error Occurred")
            }.collect {
                _verifyOTPState.value = if (it) VerifyOTPState.OldUser else VerifyOTPState.NewUser
            }
        }

    private var _otpTimeLeft = MutableStateFlow<String?>(null)
    val otpTimeLeft: StateFlow<String?>
        get() = _otpTimeLeft

    private var otpTimer: Job? = null

    private fun startOTPTimer() {
        otpTimer = viewModelScope.launch {
            var currentTime = 120
            while (isActive && currentTime >= 0) {
                val seconds = currentTime % 60
                _otpTimeLeft.value =
                    "0${currentTime / 60}:${if (seconds < 10) "0${seconds}" else seconds}"
                delay(100)
                currentTime -= 1
            }
        }
    }

}