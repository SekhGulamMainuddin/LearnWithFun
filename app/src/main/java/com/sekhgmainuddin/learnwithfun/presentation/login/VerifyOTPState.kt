package com.sekhgmainuddin.learnwithfun.presentation.login

sealed class VerifyOTPState {
    data object Initial : VerifyOTPState()
    data object Loading : VerifyOTPState()
    data object NewUser : VerifyOTPState()
    data object OldUser : VerifyOTPState()
    data class Error(val error: String) : VerifyOTPState()
}