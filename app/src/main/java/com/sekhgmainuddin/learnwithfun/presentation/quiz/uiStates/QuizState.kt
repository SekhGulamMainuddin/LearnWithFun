package com.sekhgmainuddin.learnwithfun.presentation.quiz.uiStates

import com.sekhgmainuddin.learnwithfun.common.enums.CheatingStatus
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.CheatFlag

sealed class QuizState {
    data object Initial : QuizState()
    data object NextQuestion : QuizState()
    data object SubmittingAnswer : QuizState()
    data object Completed : QuizState()
    data class Error(val message: String = "", val messageRes: Int) : QuizState()
}
