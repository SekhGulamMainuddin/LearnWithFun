package com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates

import com.sekhgmainuddin.learnwithfun.domain.modals.HomeViewContent

sealed class GetUserDetailsState {
    data object Initial : GetUserDetailsState()
    data object Loading : GetUserDetailsState()
    data class Success(val homeViewListContent: List<HomeViewContent>) : GetUserDetailsState()
    data class Error(val message: String, val messageRes: Int) : GetUserDetailsState()
}
