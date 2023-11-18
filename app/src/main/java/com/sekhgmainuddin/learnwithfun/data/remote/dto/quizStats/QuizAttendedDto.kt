package com.sekhgmainuddin.learnwithfun.data.remote.dto.quizStats

data class QuizAttendedDto(
    val totalNumberOfQuiz: Int,
    val totalQuizAttended: Int,
    val quizAttendedPercentage: Int,
    val courseName: String,
    val courseDesc: String,
    val courseThumbnail: String?,
    val courseId: String
)