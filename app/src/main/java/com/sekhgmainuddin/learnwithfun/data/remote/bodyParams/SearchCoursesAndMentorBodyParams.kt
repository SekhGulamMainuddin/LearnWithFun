package com.sekhgmainuddin.learnwithfun.data.remote.bodyParams

data class SearchCoursesAndMentorBodyParams (
    val page: Int,
    val filters: ArrayList<String>,
    val query: String
)