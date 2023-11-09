package com.sekhgmainuddin.learnwithfun.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhoneDto(
    val countryCode: Int,
    val phoneNumber: Long
)