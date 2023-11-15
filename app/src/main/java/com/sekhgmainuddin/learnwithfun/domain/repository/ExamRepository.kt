package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AddCheatFlagBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AddScoreToAttendedQuestionBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AttendExamBodyParams
import retrofit2.Response

interface ExamRepository {
    suspend fun attendExam(examBodyParams: AttendExamBodyParams): Response<com.sekhgmainuddin.learnwithfun.data.remote.dto.AttendExamDto>
    suspend fun reportCheating(addCheatFlagBodyParams: AddCheatFlagBodyParams): Response<Unit>
    suspend fun addScoreToAttendedQuestion(addScoreToAttendedQuestionBodyParams: AddScoreToAttendedQuestionBodyParams): Response<Unit>
}