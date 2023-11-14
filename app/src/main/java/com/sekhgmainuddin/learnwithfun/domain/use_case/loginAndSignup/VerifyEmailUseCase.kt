package com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup

import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getErrorMessage
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.VerifyEmailBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    private val repository: LoginSignUpRepository
) {
    operator fun invoke(verifyEmailBodyParams: VerifyEmailBodyParams) = flow<NetworkResult<Unit>> {
        try {
            val response = repository.verifyMail(verifyEmailBodyParams)
            if (response.isSuccessful) {
                emit(NetworkResult.Success(Unit))
            } else {
                emit(
                    NetworkResult.Error(
                        response.errorBody()?.getErrorMessage()!!,
                        response.code()
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