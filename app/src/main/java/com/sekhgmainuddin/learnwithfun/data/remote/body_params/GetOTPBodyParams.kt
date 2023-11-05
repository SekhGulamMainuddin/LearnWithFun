package com.sekhgmainuddin.learnwithfun.data.remote.body_params

import kotlinx.serialization.Serializable

@Serializable
data class GetOTPBodyParams(
    val countryCode: Int,
    val phoneNumber: Long
)
