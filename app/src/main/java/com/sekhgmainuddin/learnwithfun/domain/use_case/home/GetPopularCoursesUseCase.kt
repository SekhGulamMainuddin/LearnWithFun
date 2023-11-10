package com.sekhgmainuddin.learnwithfun.domain.use_case.home

import android.util.Log
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getErrorMessage
import com.sekhgmainuddin.learnwithfun.data.dto.PopularCourses
import com.sekhgmainuddin.learnwithfun.domain.modals.HomeViewContent
import com.sekhgmainuddin.learnwithfun.domain.repository.HomeRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPopularCoursesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke() = flow<NetworkResult<HomeViewContent>> {
        try {
            val response = repository.getPopularCourses()
            if (response.isSuccessful) {
                val courses = HomeViewContent(response.body()!!)
                emit(NetworkResult.Success(courses))
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
        } catch (e: Exception) {
            emit(NetworkResult.Error(strResMessage = R.string.default_error_message))
        }
    }
}