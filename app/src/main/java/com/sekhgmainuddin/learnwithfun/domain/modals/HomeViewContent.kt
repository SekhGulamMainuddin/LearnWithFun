package com.sekhgmainuddin.learnwithfun.domain.modals

import com.sekhgmainuddin.learnwithfun.data.dto.EnrolledCourse
import com.sekhgmainuddin.learnwithfun.data.dto.PopularCourse

data class HomeViewContent (
    val popularCourse: List<PopularCourse>? = null,
    val enrolledCourseProgress : EnrolledCourse? = null
)