package com.sekhgmainuddin.learnwithfun.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.use_case.login_signup.GetOTPUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.login_signup.VerifyOTPUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
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
            _getOTPState.value = GetOTPState.Sent
        }
    }

    private var _verifyOTPState = MutableStateFlow<VerifyOTPState>(VerifyOTPState.Initial)
    val verifyOTPState: StateFlow<VerifyOTPState>
        get() = _verifyOTPState

    fun verifyOTP(verifyOTPBodyParams: VerifyOTPBodyParams) =
        viewModelScope.launch(Dispatchers.IO) {
            _verifyOTPState.value = VerifyOTPState.Loading
            verifyOTPUseCase(verifyOTPBodyParams).catch {
                _verifyOTPState.value =
                    VerifyOTPState.Error(it.localizedMessage ?: "Some Error Occurred")
            }.collect {
                _verifyOTPState.value = if (it) VerifyOTPState.OldUser else VerifyOTPState.NewUser
            }
        }

}