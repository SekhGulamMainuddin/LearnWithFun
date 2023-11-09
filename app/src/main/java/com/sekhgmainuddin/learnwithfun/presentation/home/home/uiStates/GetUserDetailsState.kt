package com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates

import com.sekhgmainuddin.learnwithfun.data.dto.UserDetailDto

sealed class GetUserDetailsState {
    data object Initial : GetUserDetailsState()
    data object Loading : GetUserDetailsState()
    data class Success(val userDetailDto: UserDetailDto) : GetUserDetailsState()
    data class Error(val message: String, val messageRes: Int) : GetUserDetailsState()
}
