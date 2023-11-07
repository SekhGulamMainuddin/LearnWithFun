package com.sekhgmainuddin.learnwithfun.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDto(
    val message: String,
    val token: String,
    val user: User
)