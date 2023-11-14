package com.sekhgmainuddin.learnwithfun.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailDto(
    val enrolled_courses: List<EnrolledCourseDto>,
    val user: UserDto
) : Parcelable