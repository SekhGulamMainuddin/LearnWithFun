package com.sekhgmainuddin.learnwithfun.domain.repository

import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.CourseDetailDto
import retrofit2.Response

interface EnrollCourseRepository {
    suspend fun getCourseDetails(courseId: String) : Response<CourseDetailDto>
}