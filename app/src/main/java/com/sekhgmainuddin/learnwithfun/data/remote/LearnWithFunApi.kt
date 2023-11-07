package com.sekhgmainuddin.learnwithfun.data.remote

import com.sekhgmainuddin.learnwithfun.data.dto.CreateUserDto
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.CreateUserBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyOTPBodyParams
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LearnWithFunApi {
    @POST("/get-otp")
    suspend fun getOTP(@Body getOTP: GetOTPBodyParams)

    @POST("/verify-otp")
    suspend fun verifyOTP(@Body verifyOTPBodyParams: VerifyOTPBodyParams) : Response<HashMap<String, Any>>

    @POST("/create-user")
    suspend fun createUser(@Body createUserBodyParams: CreateUserBodyParams) : Response<CreateUserDto>

}