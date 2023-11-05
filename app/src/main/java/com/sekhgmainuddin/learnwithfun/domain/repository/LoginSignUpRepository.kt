package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.remote.body_params.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.VerifyOTPBodyParams
import retrofit2.Response

interface LoginSignUpRepository {
    suspend fun getOTP(getOTPBodyParams: GetOTPBodyParams)
    suspend fun verifyOTP(verifyOTPBodyParams: VerifyOTPBodyParams) : Response<HashMap<String, Any>>
}