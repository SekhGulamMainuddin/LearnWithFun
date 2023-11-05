package com.sekhgmainuddin.learnwithfun.presentation.login

sealed class GetOTPState {
    data object Initial : GetOTPState()
    data object Loading : GetOTPState()
    data object Sent : GetOTPState()
    data class Error(val error: String) : GetOTPState()
}