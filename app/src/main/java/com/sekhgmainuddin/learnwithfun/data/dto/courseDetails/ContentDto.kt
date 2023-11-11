package com.sekhgmainuddin.learnwithfun.data.dto.courseDetails

data class ContentDto(
    val _id: String,
    val desc: String,
    val likesIdList: List<String>,
    val notesPdfUrl: String?,
    val quiz: List<QuizDto>?,
    val thumbnail: String?,
    val title: String,
    val url: String?,
    val viewsIdList: List<String>,
    val weekNumber: Int,
    val courseDuration: String?
)