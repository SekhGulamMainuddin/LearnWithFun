package com.sekhgmainuddin.learnwithfun.presentation.login.uiStates

sealed class GetOTPState {
    data object Loading : GetOTPState()
    data object Sent : GetOTPState()
    data class Error(val message: String, val messageRes: Int) : GetOTPState()
}