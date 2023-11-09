package com.sekhgmainuddin.learnwithfun.data.dto

data class UserDto(
    val __v: Int,
    val _id: String,
    val courses: List<String>,
    val email: String,
    val name: String,
    val phone: PhoneDto,
    val profilePicture: String?,
    val userType: String
)
