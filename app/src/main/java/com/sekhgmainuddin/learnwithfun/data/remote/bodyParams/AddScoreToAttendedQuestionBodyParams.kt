package com.sekhgmainuddin.learnwithfun.data.remote.bodyParams

data class AddScoreToAttendedQuestionBodyParams(
    val examId: String,
    val questionId: String,
    val questionNumber: Int,
    val maximumMarks: Int,
    val isCorrect: Boolean,
    val isLast: Boolean,
    val courseId: String
)