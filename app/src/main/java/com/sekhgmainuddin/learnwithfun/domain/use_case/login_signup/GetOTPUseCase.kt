package com.sekhgmainuddin.learnwithfun.domain.use_case.login_signup

import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetOTPUseCase @Inject constructor(
    private val repository: LoginSignUpRepository
) {
    operator fun invoke(getOTPBodyParams: GetOTPBodyParams): Flow<Boolean> = flow {
        repository.getOTP(getOTPBodyParams)
        emit(true)
    }
}