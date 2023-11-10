package com.sekhgmainuddin.learnwithfun.domain.modals

import com.sekhgmainuddin.learnwithfun.data.dto.EnrolledCourseDto
import com.sekhgmainuddin.learnwithfun.data.dto.PopularCourseDto

data class HomeViewContent (
    val popularCourse: List<PopularCourseDto>? = null,
    val enrolledCourseProgress : EnrolledCourseDto? = null
)