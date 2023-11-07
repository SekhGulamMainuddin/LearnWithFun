package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.dto.CreateUserDto
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.CreateUserBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyOTPBodyParams
import retrofit2.Response

interface LoginSignUpRepository {
    suspend fun getOTP(getOTPBodyParams: GetOTPBodyParams)
    suspend fun verifyOTP(verifyOTPBodyParams: VerifyOTPBodyParams) : Response<HashMap<String, Any>>
    suspend fun createUser(createUserBodyParams: CreateUserBodyParams) : Response<CreateUserDto>
}