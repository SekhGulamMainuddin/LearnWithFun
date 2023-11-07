package com.sekhgmainuddin.learnwithfun.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val __v: Int,
    val _id: String,
    val courses: List<String>,
    val email: String,
    val name: String,
    val phone: Phone,
    val profilePicture: String?,
    val userType: String
)