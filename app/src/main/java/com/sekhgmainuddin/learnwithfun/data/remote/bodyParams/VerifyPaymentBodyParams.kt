package com.sekhgmainuddin.learnwithfun.data.remote.bodyParams

data class VerifyPaymentBodyParams (
    val nonce: String,
    val deviceData: String,
    val amount: String,
    val courseId: String
)