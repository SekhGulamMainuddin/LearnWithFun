package com.sekhgmainuddin.learnwithfun.data.dto

data class CreateUserDto(
    val message: String,
    val token: String,
    val user: UserDto
)