package com.sekhgmainuddin.learnwithfun.data.repository

import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.domain.repository.EnrollCourseRepository
import retrofit2.Response
import javax.inject.Inject

class EnrollCourseRepositoryImpl @Inject constructor(private val api: LearnWithFunApi) : EnrollCourseRepository{
    override suspend fun getCourseDetails(courseId: String): Response<CourseDetailDto> {
        return api.getCourseDetails(courseId)
    }
}