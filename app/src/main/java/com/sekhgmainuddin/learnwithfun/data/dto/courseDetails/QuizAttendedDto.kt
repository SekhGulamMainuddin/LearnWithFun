package com.sekhgmainuddin.learnwithfun.data.dto.courseDetails

data class QuizAttendedDto(
    val quizContentId: String,
    val examId: String?,
    val lastAttendedQuestionNumber: Int?,
    val quizCompleted: Boolean
)