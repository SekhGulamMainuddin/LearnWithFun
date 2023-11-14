package com.sekhgmainuddin.learnwithfun.domain.use_case.loginAndSignup

import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getErrorMessage
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetOTPUseCase @Inject constructor(
    private val repository: LoginSignUpRepository
) {
    operator fun invoke(getOTPBodyParams: GetOTPBodyParams) = flow<NetworkResult<Unit>> {
        try {
            emit(NetworkResult.Loading())
            val response = repository.getOTP(getOTPBodyParams)
            if (response.isSuccessful) {
                emit(NetworkResult.Success(Unit))
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