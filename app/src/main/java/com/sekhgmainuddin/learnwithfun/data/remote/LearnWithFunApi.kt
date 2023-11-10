package com.sekhgmainuddin.learnwithfun.data.remote

import com.sekhgmainuddin.learnwithfun.data.dto.CreateUserDto
import com.sekhgmainuddin.learnwithfun.data.dto.PopularCourses
import com.sekhgmainuddin.learnwithfun.data.dto.UserDetailDto
import com.sekhgmainuddin.learnwithfun.data.dto.UserDto
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.CreateUserBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyEmailBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyOTPBodyParams
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LearnWithFunApi {
    @POST("get-otp")
    suspend fun getOTP(@Body getOTP: GetOTPBodyParams) : Response<Unit>

    @POST("verify-otp")
    suspend fun verifyOTP(@Body verifyOTPBodyParams: VerifyOTPBodyParams) : Response<HashMap<String, Any>>

    @POST("user")
    suspend fun createUser(@Body createUserBodyParams: CreateUserBodyParams) : Response<CreateUserDto>

    @GET("send-mail")
    suspend fun sendMail(@Query("email") email: String) : Response<Unit>

    @POST("verify-mail")
    suspend fun verifyMail(@Body verifyEmailBodyParams: VerifyEmailBodyParams) : Response<Unit>

    @GET("user")
    suspend fun getUserDetails() : Response<UserDetailDto>

    @GET("course/get-recommended-courses")
    suspend fun getPopularCourses() : Response<PopularCourses>

}