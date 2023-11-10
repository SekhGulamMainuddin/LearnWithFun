package com.sekhgmainuddin.learnwithfun.domain.use_case.home

import android.util.Log
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getErrorMessage
import com.sekhgmainuddin.learnwithfun.data.dto.UserDetailDto
import com.sekhgmainuddin.learnwithfun.domain.repository.HomeRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke() = flow<NetworkResult<UserDetailDto>> {
        try {
            emit(NetworkResult.Loading())
            val response = repository.getUserDetails()
            if (response.isSuccessful) {
                val userDetails = response.body()!!
                userDetails.enrolled_courses.map {
                    it.courseCoverageProgress =
                        (if (it.courseLength != 0) (it.courseCoverage.toDouble() / it.courseLength.toDouble() * 100.0) else 0.0).toInt()
                }
                emit(NetworkResult.Success(userDetails))
            } else {
                emit(
                    NetworkResult.Error(
                        response.errorBody()?.getErrorMessage() ?: "",
                        response.code()
                    )
                )
            }
        } catch (e: IOException) {
            emit(NetworkResult.Error(strResMessage = R.string.no_internet_please_check_your_internet_connection))
        } catch (_: Exception) {
            emit(NetworkResult.Error(strResMessage = R.string.default_error_message))
        }
    }
}