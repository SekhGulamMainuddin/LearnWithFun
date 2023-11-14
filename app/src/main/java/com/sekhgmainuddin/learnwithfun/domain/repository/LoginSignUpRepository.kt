package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.dto.CreateUserDto
import com.sekhgmainuddin.learnwithfun.data.dto.GeneralResponseDto
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.CreateUserBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.VerifyEmailBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.VerifyOTPBodyParams
import retrofit2.Response

interface LoginSignUpRepository {
    suspend fun getOTP(getOTPBodyParams: GetOTPBodyParams) : Response<Unit>
    suspend fun verifyOTP(verifyOTPBodyParams: VerifyOTPBodyParams) : Response<HashMap<String, Any>>
    suspend fun createUser(createUserBodyParams: CreateUserBodyParams) : Response<CreateUserDto>
    suspend fun sendMail(email: String) : Response<Unit>
    suspend fun verifyMail(verifyEmailBodyParams: VerifyEmailBodyParams) : Response<Unit>
}