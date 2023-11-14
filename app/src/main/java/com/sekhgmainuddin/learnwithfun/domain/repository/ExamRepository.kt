package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.dto.AttendExamDto
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.AddCheatFlagBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.AddScoreToAttendedQuestionBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.AttendExamBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.CheatFlag
import retrofit2.Response

interface ExamRepository {
    suspend fun attendExam(examBodyParams: AttendExamBodyParams): Response<AttendExamDto>
    suspend fun reportCheating(addCheatFlagBodyParams: AddCheatFlagBodyParams): Response<Unit>
    suspend fun addScoreToAttendedQuestion(addScoreToAttendedQuestionBodyParams: AddScoreToAttendedQuestionBodyParams): Response<Unit>
}