package com.sekhgmainuddin.learnwithfun.presentation.login.uiStates

sealed class VerifyOTPState {
    data object Initial : VerifyOTPState()
    data object Loading : VerifyOTPState()
    data object NewUser : VerifyOTPState()
    data object OldUser : VerifyOTPState()
    data object WrongOTP : VerifyOTPState()
    data class Error(val error: String) : VerifyOTPState()
}