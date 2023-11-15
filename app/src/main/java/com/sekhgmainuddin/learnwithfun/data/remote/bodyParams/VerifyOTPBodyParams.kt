package com.sekhgmainuddin.learnwithfun.data.remote.bodyParams

data class VerifyOTPBodyParams(
    val countryCode: Int,
    val phoneNumber: Long,
    val otp: String
)
