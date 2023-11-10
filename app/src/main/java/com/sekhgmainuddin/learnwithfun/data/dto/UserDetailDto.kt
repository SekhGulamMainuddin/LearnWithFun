package com.sekhgmainuddin.learnwithfun.data.dto

data class UserDetailDto(
    val enrolled_courses: List<EnrolledCourseDto>,
    val user: UserDto
)