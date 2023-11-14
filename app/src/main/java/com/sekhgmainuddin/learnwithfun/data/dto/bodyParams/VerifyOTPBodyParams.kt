package com.sekhgmainuddin.learnwithfun.data.dto.bodyParams

data class VerifyOTPBodyParams(
    val countryCode: Int,
    val phoneNumber: Long,
    val otp: String
)
