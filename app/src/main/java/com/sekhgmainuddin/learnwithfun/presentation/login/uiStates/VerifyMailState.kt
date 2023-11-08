package com.sekhgmainuddin.learnwithfun.presentation.login.uiStates

sealed class VerifyMailState{
    data object Loading : VerifyMailState()
    data object EmailNotFound : VerifyMailState()
    data object VerificationCodeNotEntered : VerifyMailState()
    data object WrongVerificationCode : VerifyMailState()
    data object Success : VerifyMailState()
    data class Error(val message: String, val messageRes: Int) : VerifyMailState()
}
