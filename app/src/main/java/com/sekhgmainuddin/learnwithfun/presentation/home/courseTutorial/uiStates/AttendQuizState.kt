package com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.uiStates

sealed class AttendQuizState{
    data object Initial: AttendQuizState()
    data object Loading: AttendQuizState()
    data class Success(val examId: String): AttendQuizState()
    data class Error(val message: String, val messageRes: Int): AttendQuizState()
}
