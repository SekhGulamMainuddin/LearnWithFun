package com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse.uiStates

import com.sekhgmainuddin.learnwithfun.domain.modals.SearchItem

sealed class CoursesState{
    data object Initial : CoursesState()
    data object Loading : CoursesState()
    data class Loaded(val list: List<SearchItem>) : CoursesState()
    data class Error(val message: String, val messageRes: Int) : CoursesState()
}
