package com.sekhgmainuddin.learnwithfun.data.repository

import com.sekhgmainuddin.learnwithfun.data.dto.PopularCoursesDto
import com.sekhgmainuddin.learnwithfun.data.dto.UserDetailDto
import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.domain.repository.HomeRepository
import retrofit2.Response
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: LearnWithFunApi
) : HomeRepository {
    override suspend fun getUserDetails(): Response<UserDetailDto> {
        return api.getUserDetails()
    }

    override suspend fun getPopularCourses(): Response<PopularCoursesDto> {
        return api.getPopularCourses()
    }
}