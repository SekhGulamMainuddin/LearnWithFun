package com.sekhgmainuddin.learnwithfun.domain.use_case.quiz

import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getErrorMessage
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AddScoreToAttendedQuestionBodyParams
import com.sekhgmainuddin.learnwithfun.domain.repository.ExamRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AddCorrectAnsScoreUseCase @Inject constructor(
    private val repository: ExamRepository
) {
    operator fun invoke(body: AddScoreToAttendedQuestionBodyParams) = flow<NetworkResult<Unit>> {
        try {
            emit(NetworkResult.Loading())
            val response = repository.addScoreToAttendedQuestion(body)
            if (response.isSuccessful) {
                emit(NetworkResult.Success(Unit))
            } else {
                emit(NetworkResult.Error(response.errorBody()!!.getErrorMessage()))
            }
        } catch (e: IOException) {
            emit(NetworkResult.Error(strResMessage = R.string.no_internet_please_check_your_internet_connection))
        } catch (_: Exception) {
            emit(NetworkResult.Error(strResMessage = R.string.default_error_message))
        }
    }
}