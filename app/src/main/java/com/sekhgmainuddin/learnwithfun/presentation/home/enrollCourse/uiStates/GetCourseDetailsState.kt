package com.sekhgmainuddin.learnwithfun.presentation.home.enrollCourse.uiStates

import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.CourseDetailDto

sealed class GetCourseDetailsState {
    data object Initial : GetCourseDetailsState()
    data object Loading : GetCourseDetailsState()
    data class Success(val courseDetailDto: CourseDetailDto) : GetCourseDetailsState()
    data class Error(val message: String, val messageRes: Int) : GetCourseDetailsState()
}
