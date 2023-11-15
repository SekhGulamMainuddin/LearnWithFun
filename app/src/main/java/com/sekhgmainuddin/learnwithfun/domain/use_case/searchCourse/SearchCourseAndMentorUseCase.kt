package com.sekhgmainuddin.learnwithfun.domain.use_case.searchCourse

import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getErrorMessage
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.SearchCoursesAndMentorBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.CourseAndUserSearchDto
import com.sekhgmainuddin.learnwithfun.domain.repository.CourseRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SearchCourseAndMentorUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    operator fun invoke(coursesAndMentorBodyParams: SearchCoursesAndMentorBodyParams) =
        flow<NetworkResult<CourseAndUserSearchDto>> {
            try {
                emit(NetworkResult.Loading())
                val response = courseRepository.searchCoursesAndMentors(coursesAndMentorBodyParams)
                if (response.isSuccessful) {
                    emit(NetworkResult.Success(response.body()!!))
                } else {
                    emit(
                        NetworkResult.Error(
                            statusCode = response.code(),
                            message = response.errorBody()!!.getErrorMessage()
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