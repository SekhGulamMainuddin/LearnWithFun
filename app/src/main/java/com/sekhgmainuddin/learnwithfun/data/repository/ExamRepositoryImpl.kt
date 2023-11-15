package com.sekhgmainuddin.learnwithfun.data.repository

import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AddCheatFlagBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AddScoreToAttendedQuestionBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AttendExamBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.domain.repository.ExamRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamRepositoryImpl @Inject constructor(private val api: LearnWithFunApi) : ExamRepository {
    override suspend fun attendExam(examBodyParams: AttendExamBodyParams): Response<com.sekhgmainuddin.learnwithfun.data.remote.dto.AttendExamDto> {
        return api.attendExam(examBodyParams)
    }

    override suspend fun reportCheating(addCheatFlagBodyParams: AddCheatFlagBodyParams): Response<Unit> {
        return api.addCheatFlag(addCheatFlagBodyParams)
    }

    override suspend fun addScoreToAttendedQuestion(addScoreToAttendedQuestionBodyParams: AddScoreToAttendedQuestionBodyParams): Response<Unit> {
        return api.addScoreToAttendedQuestion(addScoreToAttendedQuestionBodyParams)
    }
}