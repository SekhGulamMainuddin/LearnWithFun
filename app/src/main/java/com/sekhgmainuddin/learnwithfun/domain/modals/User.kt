package com.sekhgmainuddin.learnwithfun.domain.modals

import com.sekhgmainuddin.learnwithfun.data.dto.PhoneDto

data class User(
    val id: String,
    val courses: List<Courses>,
    val email: String,
    val name: String,
    val phone: PhoneDto,
    val profilePicture: String = "",
    val userType: String
)