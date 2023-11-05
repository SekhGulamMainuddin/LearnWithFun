package com.sekhgmainuddin.learnwithfun.data.remote.body_params

data class VerifyOTPBodyParams(
    val countryCode: Int,
    val phoneNumber: Long,
    val otp: String
)
