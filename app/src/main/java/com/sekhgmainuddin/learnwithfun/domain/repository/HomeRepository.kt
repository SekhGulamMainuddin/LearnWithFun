package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.UpdateActivityBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.PopularCoursesDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.UserDetailDto
import retrofit2.Response

interface HomeRepository {
    suspend fun getUserDetails() : Response<UserDetailDto>
    suspend fun getPopularCourses() : Response<PopularCoursesDto>
    suspend fun updateActivity(updateActivityBodyParams: UpdateActivityBodyParams)
}