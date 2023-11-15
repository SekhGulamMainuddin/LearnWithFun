package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.SearchCoursesAndMentorBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.CourseAndUserSearchDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.CourseDetailDto
import retrofit2.Response

interface CourseRepository {
    suspend fun getCourseDetails(courseId: String): Response<CourseDetailDto>
    suspend fun searchCoursesAndMentors(searchCoursesAndMentorBodyParams: SearchCoursesAndMentorBodyParams): Response<CourseAndUserSearchDto>
}