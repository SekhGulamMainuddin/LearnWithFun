package com.sekhgmainuddin.learnwithfun.presentation.quiz.uiStates

sealed class QuizState {
    data object Initial : QuizState()
    data object NextQuestion : QuizState()
    data object SubmittingAnswer : QuizState()
    data object Completed : QuizState()
    data class Error(val message: String = "", val messageRes: Int) : QuizState()
}
