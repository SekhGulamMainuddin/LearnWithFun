package com.sekhgmainuddin.learnwithfun.presentation.login.uiStates

sealed class CreateUserState {
    data object Initial : CreateUserState()
    data object Loading : CreateUserState()
    data object NameNotAdded : CreateUserState()
    data object ProfilePicUploadFailed : CreateUserState()
    data object EmailNotVerified : CreateUserState()
    data object UserTypeNotSpecified : CreateUserState()
    data object UserCreated : CreateUserState()
    data class Error(val message: String) : CreateUserState()
}