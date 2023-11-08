package com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup

import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.helper.PrefsHelper
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getErrorMessage
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class VerifyOTPUseCase @Inject constructor(
    private val repository: LoginSignUpRepository,
    private val prefsHelper: PrefsHelper
) {
    operator fun invoke(
        verifyOTPBodyParams: VerifyOTPBodyParams
    ) = flow<NetworkResult<Boolean>> {
        try {
            val response = repository.verifyOTP(verifyOTPBodyParams)
            if (response.isSuccessful) {
                val token = response.body()?.get("token") as String?
                if (token != null) {
                    prefsHelper.updateToken(token)
                }
                emit(NetworkResult.Success(token != null))
            } else {
                emit(
                    NetworkResult.Error(
                        response.errorBody()?.getErrorMessage() ?: "",
                        statusCode = response.code()
                    )
                )
            }
        } catch (_: IOException) {
            emit(NetworkResult.Error(strResMessage = R.string.no_internet_please_check_your_internet_connection))
        } catch (e: Exception) {
            emit(NetworkResult.Error(strResMessage = R.string.default_error_message))
        }
    }
}