package com.sekhgmainuddin.learnwithfun.domain.modals

import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.QuizDto

data class Quiz(
    val quiz: QuizDto,
    val userClick: Int = -1,
    val correctAns: Int? = null
)