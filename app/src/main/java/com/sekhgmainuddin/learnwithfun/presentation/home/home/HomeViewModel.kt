package com.sekhgmainuddin.learnwithfun.presentation.home.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.data.dto.UserDetailDto
import com.sekhgmainuddin.learnwithfun.domain.modals.HomeViewContent
import com.sekhgmainuddin.learnwithfun.domain.use_case.home.GetPopularCoursesUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.home.GetUserDetailsUseCase
import com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates.GetPopularCoursesState
import com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates.GetUserDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val getPopularCoursesUseCase: GetPopularCoursesUseCase
) : ViewModel() {

    private var _userDetails = MutableStateFlow<GetUserDetailsState>(GetUserDetailsState.Initial)
    val userDetails: StateFlow<GetUserDetailsState>
        get() = _userDetails

    val userDetail = MutableStateFlow<UserDetailDto?>(null)

    private var popularCourseList = HomeViewContent(emptyList())
    private var homeViewContent = mutableListOf<HomeViewContent>()

    fun getUserDetails() = viewModelScope.launch(Dispatchers.IO) {
        getUserDetailsUseCase().collect {
            when (it) {
                is NetworkResult.Success -> {
                    userDetail.value = it.data!!
                    val contentList = it.data!!.enrolled_courses.map { course ->
                        HomeViewContent(
                            enrolledCourseProgress = course
                        )
                    }.toList()
                    homeViewContent.clear()
                    homeViewContent.add(popularCourseList)
                    homeViewContent.add(HomeViewContent())
                    homeViewContent.addAll(contentList)
                    // Done because ListAdapter DiffUtil doesn't call on same list
                    _userDetails.value = GetUserDetailsState.Success(homeViewContent.toList())
                }

                is NetworkResult.Loading -> {
                    _userDetails.value = GetUserDetailsState.Loading
                }

                is NetworkResult.Error -> {
                    _userDetails.value = GetUserDetailsState.Error(
                        it.message,
                        it.strResMessage ?: R.string.default_error_message
                    )
                }
            }
        }
    }

    private var _popularCourses =
        MutableStateFlow<GetPopularCoursesState>(GetPopularCoursesState.Initial)
    val popularCourses: StateFlow<GetPopularCoursesState>
        get() = _popularCourses

    fun getPopularCourses() = viewModelScope.launch(Dispatchers.IO) {
        getPopularCoursesUseCase().collect {
            when (it) {
                is NetworkResult.Success -> {
                    popularCourseList = it.data!!
                    if(homeViewContent.isNotEmpty()){
                        homeViewContent[0] = popularCourseList
                        _popularCourses.value = GetPopularCoursesState.Success(homeViewContent.toList())
                    }
                    // Done because ListAdapter DiffUtil doesn't call on same list
                }

                is NetworkResult.Loading -> {
                    _popularCourses.value = GetPopularCoursesState.Loading
                }

                is NetworkResult.Error -> {
                    _popularCourses.value = GetPopularCoursesState.Error(
                        it.message,
                        it.strResMessage ?: R.string.default_error_message
                    )
                }
            }
        }
    }


}