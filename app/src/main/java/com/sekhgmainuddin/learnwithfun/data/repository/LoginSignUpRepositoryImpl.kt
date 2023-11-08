package com.sekhgmainuddin.learnwithfun.data.repository

import com.sekhgmainuddin.learnwithfun.data.dto.CreateUserDto
import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.CreateUserBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyEmailBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import retrofit2.Response
import javax.inject.Inject

class LoginSignUpRepositoryImpl @Inject constructor(
    private val api: LearnWithFunApi
) :
    LoginSignUpRepository {
    override suspend fun getOTP(getOTPBodyParams: GetOTPBodyParams): Response<Unit> {
        return api.getOTP(getOTPBodyParams)
    }

    override suspend fun verifyOTP(verifyOTPBodyParams: VerifyOTPBodyParams): Response<HashMap<String, Any>> {
        return api.verifyOTP(verifyOTPBodyParams)
    }

    override suspend fun createUser(createUserBodyParams: CreateUserBodyParams): Response<CreateUserDto> {
        return api.createUser(createUserBodyParams)
    }

    override suspend fun sendMail(email: String): Response<Unit> {
        return api.sendMail(email)
    }

    override suspend fun verifyMail(verifyEmailBodyParams: VerifyEmailBodyParams): Response<Unit> {
        return api.verifyMail(verifyEmailBodyParams)
    }
}