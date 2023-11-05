package com.sekhgmainuddin.learnwithfun.domain.use_case.login_signup

import com.sekhgmainuddin.learnwithfun.common.helper.PrefsHelper
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerifyOTPUseCase @Inject constructor(
    private val repository: LoginSignUpRepository,
    private val prefsHelper: PrefsHelper
) {
    operator fun invoke(
        verifyOTPBodyParams: VerifyOTPBodyParams
    ): Flow<Boolean> = flow {
        val response = repository.verifyOTP(verifyOTPBodyParams)
        val token = response.body()?.get("token") as String?
        if (token != null) {
            prefsHelper.updateToken(token)
        }
        emit(token != null)
    }
}