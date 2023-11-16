package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyPaymentBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.PaymentTokenDto
import retrofit2.Response

interface PaymentRepository {
    suspend fun getPaymentToken() : Response<PaymentTokenDto>
    suspend fun verifyPayment(verifyPaymentBodyParams: VerifyPaymentBodyParams) : Response<Unit>
}