package com.sekhgmainuddin.learnwithfun.data.repository

import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import retrofit2.Response
import javax.inject.Inject

class LoginSignUpRepositoryImpl @Inject constructor(private val api: LearnWithFunApi) : LoginSignUpRepository {
    override suspend fun getOTP(getOTPBodyParams: GetOTPBodyParams) {
        return api.getOTP(getOTPBodyParams)
    }

    override suspend fun verifyOTP(verifyOTPBodyParams: VerifyOTPBodyParams) : Response<HashMap<String, Any>> {
        return api.verifyOTP(verifyOTPBodyParams)
    }
}