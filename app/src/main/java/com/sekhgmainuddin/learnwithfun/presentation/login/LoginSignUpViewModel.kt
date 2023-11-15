package com.sekhgmainuddin.learnwithfun.presentation.login

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.enums.UserType
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.isValidEmail
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyEmailBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup.CreateUserUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup.GetOTPUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup.SendEmailUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup.VerifyEmailUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup.VerifyOTPUseCase
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.CreateUserState
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.GetOTPState
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.SendMailState
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.VerifyMailState
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.VerifyOTPState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSignUpViewModel @Inject constructor(
    private val getOTPUseCase: GetOTPUseCase,
    private val verifyOTPUseCase: VerifyOTPUseCase,
    private val sendMailUseCase: SendEmailUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private var _getOTPState = MutableSharedFlow<GetOTPState>()
    val getOTPState: SharedFlow<GetOTPState>
        get() = _getOTPState

    fun getOTP(getOTPBodyParams: GetOTPBodyParams) = viewModelScope.launch(Dispatchers.IO) {
        _getOTPState.emit(GetOTPState.Loading)
        getOTPUseCase(getOTPBodyParams).collect {
            when (it) {
                is NetworkResult.Success -> {
                    _getOTPState.emit(GetOTPState.Sent)
                    startOTPTimer()
                }

                is NetworkResult.Error -> _getOTPState.emit(
                    GetOTPState.Error(
                        it.message,
                        it.strResMessage ?: R.string.default_error_message
                    )
                )

                is NetworkResult.Loading -> _getOTPState.emit(GetOTPState.Loading)
            }
        }
    }

    private var _verifyOTPState = MutableSharedFlow<VerifyOTPState>()
    val verifyOTPState: SharedFlow<VerifyOTPState>
        get() = _verifyOTPState

    fun verifyOTP(verifyOTPBodyParams: VerifyOTPBodyParams) =
        viewModelScope.launch(Dispatchers.IO) {
            otpTimer?.cancel()
            _verifyOTPState.emit(VerifyOTPState.Loading)
            verifyOTPUseCase(verifyOTPBodyParams).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _verifyOTPState.emit(if (it.data == true) VerifyOTPState.OldUser else VerifyOTPState.NewUser)
                        _otpTimeLeft.value = null
                    }
                    is NetworkResult.Loading -> _verifyOTPState.emit(VerifyOTPState.Loading)
                    is NetworkResult.Error -> {
                        if (it.statusCode == 403) {
                            _verifyOTPState.emit(VerifyOTPState.WrongOTP)
                        } else {
                            _verifyOTPState.emit(
                                VerifyOTPState.Error(
                                    it.message,
                                    it.strResMessage ?: R.string.default_error_message
                                )
                            )
                        }
                    }
                }
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
                delay(1000)
                currentTime -= 1
            }
        }
    }

    private var _sendMailState = MutableSharedFlow<SendMailState>()
    val sendMailState: SharedFlow<SendMailState>
        get() = _sendMailState

    fun sendMail(email: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (!email.isValidEmail()) {
            _sendMailState.emit(SendMailState.InvalidEmail)
        } else {
            sendMailUseCase(email!!).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _sendMailState.emit(SendMailState.Success)
                    }

                    is NetworkResult.Loading -> {
                        _sendMailState.emit(SendMailState.Loading)
                    }

                    is NetworkResult.Error -> {
                        if (it.statusCode == 403) {
                            _sendMailState.emit(SendMailState.EmailAlreadyTaken)
                        } else {
                            _sendMailState.emit(
                                SendMailState.Error(
                                    it.message,
                                    it.strResMessage ?: R.string.default_error_message
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private var _verifyMailState = MutableSharedFlow<VerifyMailState>()
    val verifyMailState: SharedFlow<VerifyMailState>
        get() = _verifyMailState

    fun verifyMail(email: String, verificationCode: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            if (verificationCode.isNullOrEmpty()) {
                _verifyMailState.emit(VerifyMailState.VerificationCodeNotEntered)
            } else {
                verifyEmailUseCase(VerifyEmailBodyParams(email, verificationCode)).collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            _verifyMailState.emit(VerifyMailState.Success)
                        }

                        is NetworkResult.Loading -> {
                            _verifyMailState.emit(VerifyMailState.Loading)
                        }

                        is NetworkResult.Error -> {
                            when (it.statusCode) {
                                404 -> {
                                    _verifyMailState.emit(VerifyMailState.EmailNotFound)
                                }

                                403 -> {
                                    _verifyMailState.emit(VerifyMailState.WrongVerificationCode)
                                }

                                else -> {
                                    _verifyMailState
                                        .emit(
                                            VerifyMailState.Error(
                                                it.message,
                                                it.strResMessage ?: R.string.default_error_message
                                            )
                                        )
                                }
                            }
                        }
                    }
                }
            }
        }

    private var _createUserState = MutableSharedFlow<CreateUserState>()
    val createUserState: SharedFlow<CreateUserState>
        get() = _createUserState

    fun createUser(
        name: String,
        email: String,
        countryCode: Int,
        phoneNumber: Long,
        profilePicture: Bitmap?
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            if (name.isEmpty()) {
                _createUserState.emit(CreateUserState.NameNotAdded)
            } else if (email.isEmpty()) {
                _createUserState.emit(CreateUserState.EmailNotVerified)
            } else if (userType.value == null) {
                _createUserState.emit(CreateUserState.UserTypeNotSpecified)
            } else {
                _createUserState.emit(CreateUserState.Loading)
                createUserUseCase(
                    name,
                    email,
                    countryCode,
                    phoneNumber,
                    userType.value!!,
                    profilePicture
                ).collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            _createUserState.emit(CreateUserState.UserCreated)
                        }

                        is NetworkResult.Loading -> {
                            _createUserState.emit(CreateUserState.Loading)
                        }

                        is NetworkResult.Error -> {
                            _createUserState.emit(CreateUserState.Error(it.message))
                        }
                    }
                }
            }
        }

    val userType = MutableStateFlow<UserType?>(null)
    fun changeUserType(type: UserType) {
        if (userType.value == type) {
            userType.value = null
        } else {
            userType.value = type
        }
        Log.d("USERTYPE", "changeUserType: $userType")
    }

}