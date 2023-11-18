package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.remote.dto.quizStats.QuizStatsDto
import retrofit2.Response

interface ProfileRepository {
    suspend fun getQuizStats() : Response<QuizStatsDto>
}