package com.sekhgmainuddin.learnwithfun.data.remote.dto

data class CreateUserDto(
    val message: String,
    val token: String,
    val user: UserDto
)