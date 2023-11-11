package com.sekhgmainuddin.learnwithfun.presentation.home.couses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.domain.use_case.enrollCourse.GetCourseDetailsUseCase
import com.sekhgmainuddin.learnwithfun.presentation.home.enrollCourse.uiStates.GetCourseDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val getCourseDetailsUseCase: GetCourseDetailsUseCase
) : ViewModel() {

    private var _courseDetails =
        MutableStateFlow<GetCourseDetailsState>(GetCourseDetailsState.Initial)
    val courseDetails: StateFlow<GetCourseDetailsState>
        get() = _courseDetails

    val courseDetail = MutableStateFlow<CourseDetailDto?>(null)

    fun getCourseDetails(courseId: String) = viewModelScope.launch(Dispatchers.IO) {
        courseDetail.value = null
        getCourseDetailsUseCase(courseId).collect {
            when (it) {
                is NetworkResult.Success -> {
                    courseDetail.value = it.data
                    _courseDetails.value = GetCourseDetailsState.Success(it.data!!)
                }

                is NetworkResult.Loading -> {
                    _courseDetails.value = GetCourseDetailsState.Loading
                }

                is NetworkResult.Error -> {
                    Log.d("asjdkasjgd", "getCourseDetails: ${it.message}")
                    _courseDetails.value = GetCourseDetailsState.Error(
                        it.message,
                        it.strResMessage ?: R.string.default_error_message
                    )
                }
            }
        }
    }

}