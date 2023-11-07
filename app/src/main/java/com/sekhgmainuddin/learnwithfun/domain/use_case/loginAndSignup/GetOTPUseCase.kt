package com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup

import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOTPUseCase @Inject constructor(
    private val repository: LoginSignUpRepository
) {
    operator fun invoke(getOTPBodyParams: GetOTPBodyParams) = flow {
        repository.getOTP(getOTPBodyParams)
        emit(Unit)
    }
}