package com.sekhgmainuddin.learnwithfun.data.repository

import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyPaymentBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.PaymentTokenDto
import com.sekhgmainuddin.learnwithfun.domain.repository.PaymentRepository
import retrofit2.Response
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val api: LearnWithFunApi) : PaymentRepository {
    override suspend fun getPaymentToken(): Response<PaymentTokenDto> {
        return api.getPaymentToken()
    }

    override suspend fun verifyPayment(verifyPaymentBodyParams: VerifyPaymentBodyParams): Response<Unit> {
        return api.verifyPayment(verifyPaymentBodyParams)
    }
}