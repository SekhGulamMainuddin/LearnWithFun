package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.dto.PopularCourses
import com.sekhgmainuddin.learnwithfun.data.dto.UserDetailDto
import retrofit2.Response

interface HomeRepository {
    suspend fun getUserDetails() : Response<UserDetailDto>
    suspend fun getPopularCourses() : Response<PopularCourses>
}