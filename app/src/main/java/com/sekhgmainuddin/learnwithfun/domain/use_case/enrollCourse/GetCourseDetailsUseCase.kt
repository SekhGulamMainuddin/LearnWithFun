package com.sekhgmainuddin.learnwithfun.domain.use_case.enrollCourse

import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getErrorMessage
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.domain.repository.CourseRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetCourseDetailsUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    operator fun invoke(courseId: String) = flow<NetworkResult<CourseDetailDto>> {
        try {
            val response = repository.getCourseDetails(courseId)
            if (response.isSuccessful) {
                emit(NetworkResult.Success(response.body()!!))
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