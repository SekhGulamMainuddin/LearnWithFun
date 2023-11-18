package com.sekhgmainuddin.learnwithfun.data.repository

import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.data.remote.dto.quizStats.QuizStatsDto
import com.sekhgmainuddin.learnwithfun.domain.repository.ProfileRepository
import retrofit2.Response
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val api: LearnWithFunApi) :
    ProfileRepository {
    override suspend fun getQuizStats(): Response<QuizStatsDto> {
        return api.getQuizStats()
    }
}