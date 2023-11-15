package com.sekhgmainuddin.learnwithfun.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserDto(
    val __v: Int,
    val _id: String,
    val courses: List<String>,
    val email: String,
    val name: String,
    val phone: PhoneDto,
    val profilePicture: String?,
    val userType: String
) : Parcelable
