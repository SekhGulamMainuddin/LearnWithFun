package com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates

import com.sekhgmainuddin.learnwithfun.domain.modals.HomeViewContent

sealed class GetPopularCoursesState {
    data object Initial : GetPopularCoursesState()
    data object Loading : GetPopularCoursesState()
    data class Success(val content: List<HomeViewContent>) : GetPopularCoursesState()
    data class Error(val message: String, val messageRes: Int) : GetPopularCoursesState()
}