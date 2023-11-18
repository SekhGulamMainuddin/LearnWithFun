package com.sekhgmainuddin.learnwithfun.data.remote.dto.quizStats

data class QuizStatsDto(
    val overallPercentage: Int,
    val quizAttendedList: ArrayList<QuizAttendedDto>,
)