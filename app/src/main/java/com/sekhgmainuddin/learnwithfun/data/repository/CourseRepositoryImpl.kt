package com.sekhgmainuddin.learnwithfun.data.repository

import com.sekhgmainuddin.learnwithfun.data.remote.LearnWithFunApi
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.SearchCoursesAndMentorBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.CourseAndUserSearchDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.domain.repository.CourseRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepositoryImpl @Inject constructor(private val api: LearnWithFunApi) : CourseRepository{
    override suspend fun getCourseDetails(courseId: String): Response<CourseDetailDto> {
        return api.getCourseDetails(courseId)
    }

    override suspend fun searchCoursesAndMentors(searchCoursesAndMentorBodyParams: SearchCoursesAndMentorBodyParams): Response<CourseAndUserSearchDto> {
        return api.getCoursesAndMentorsForQuery(searchCoursesAndMentorBodyParams)
    }
}