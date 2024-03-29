package com.sekhgmainuddin.learnwithfun.presentation.login.uiStates

sealed class VerifyOTPState {
    data object Loading : VerifyOTPState()
    data object NewUser : VerifyOTPState()
    data object OldUser : VerifyOTPState()
    data object WrongOTP : VerifyOTPState()
    data class Error(val message: String, val messageRes: Int) : VerifyOTPState()
}