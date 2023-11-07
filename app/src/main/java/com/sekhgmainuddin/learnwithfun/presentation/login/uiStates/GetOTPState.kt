package com.sekhgmainuddin.learnwithfun.presentation.login.uiStates

sealed class GetOTPState {
    data object Initial : GetOTPState()
    data object Loading : GetOTPState()
    data object Sent : GetOTPState()
    data class Error(val error: String) : GetOTPState()
}