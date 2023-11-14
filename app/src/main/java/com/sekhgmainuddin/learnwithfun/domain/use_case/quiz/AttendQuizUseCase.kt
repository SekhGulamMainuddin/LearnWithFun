package com.sekhgmainuddin.learnwithfun.domain.use_case.quiz

import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getErrorMessage
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.AttendExamBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.ExamRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AttendQuizUseCase @Inject constructor(private val repository: ExamRepository) {
    operator fun invoke(attendExamBodyParams: AttendExamBodyParams) = flow<NetworkResult<String>> {
        try {
            val response = repository.attendExam(attendExamBodyParams)
            if (response.isSuccessful) {
                emit(NetworkResult.Success(response.body()!!.examId))
            } else {
                emit(
                    NetworkResult.Error(
                        statusCode = response.code(),
                        message = response.errorBody()?.getErrorMessage() ?: ""
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