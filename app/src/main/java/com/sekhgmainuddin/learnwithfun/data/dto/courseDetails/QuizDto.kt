package com.sekhgmainuddin.learnwithfun.data.dto.courseDetails

data class QuizDto(
    val _id: String,
    val correctAns: Int,
    val image: String,
    val marks: Int,
    val options: List<OptionDto>,
    val question: String,
    val questionDesc: String,
    val typeOfQuestion: String
)