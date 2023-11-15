package com.sekhgmainuddin.learnwithfun.data.remote.dto

data class CourseAndUserSearchDto(
    val courseList: ArrayList<CourseDtoItem>,
    val users: ArrayList<UserDto>
)