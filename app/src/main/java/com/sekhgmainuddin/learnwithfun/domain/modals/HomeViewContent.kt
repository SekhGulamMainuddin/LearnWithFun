package com.sekhgmainuddin.learnwithfun.domain.modals

import com.sekhgmainuddin.learnwithfun.data.remote.dto.EnrolledCourseDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.PopularCourseDto

data class HomeViewContent (
    val popularCourse: List<PopularCourseDto>? = null,
    val enrolledCourseProgress : EnrolledCourseDto? = null
)