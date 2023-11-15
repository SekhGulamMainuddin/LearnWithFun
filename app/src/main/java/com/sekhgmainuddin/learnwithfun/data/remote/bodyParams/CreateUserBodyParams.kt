package com.sekhgmainuddin.learnwithfun.data.remote.bodyParams

data class CreateUserBodyParams(
    val name: String,
    val email: String,
    val countryCode: Int,
    val phoneNumber: Long,
    val userType: String,
    val profilePicture: String?
)