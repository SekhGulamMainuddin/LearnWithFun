package com.sekhgmainuddin.learnwithfun.presentation.login.uiStates

sealed class SendMailState {
    data object Loading : SendMailState()
    data object Success : SendMailState()
    data object InvalidEmail : SendMailState()
    data object EmailAlreadyTaken : SendMailState()
    data class Error(val message: String, val messageRes: Int) : SendMailState()
}
