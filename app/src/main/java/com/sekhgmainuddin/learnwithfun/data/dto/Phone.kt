package com.sekhgmainuddin.learnwithfun.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Phone(
    val countryCode: Int,
    val phoneNumber: Long
)