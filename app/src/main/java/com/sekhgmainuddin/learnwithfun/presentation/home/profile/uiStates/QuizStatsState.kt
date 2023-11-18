package com.sekhgmainuddin.learnwithfun.presentation.home.profile.uiStates

import com.sekhgmainuddin.learnwithfun.data.remote.dto.quizStats.QuizStatsDto

sealed class QuizStatsState {
    data object Initial : QuizStatsState()
    data object Loading : QuizStatsState()
    data class Success(val stats: QuizStatsDto) : QuizStatsState()
    data class Error(val message: String, val messageRes: Int) : QuizStatsState()
}
