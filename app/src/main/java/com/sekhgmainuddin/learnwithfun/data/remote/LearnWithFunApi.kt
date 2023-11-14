package com.sekhgmainuddin.learnwithfun.data.remote

import com.sekhgmainuddin.learnwithfun.data.dto.AttendExamDto
import com.sekhgmainuddin.learnwithfun.data.dto.CreateUserDto
import com.sekhgmainuddin.learnwithfun.data.dto.PopularCoursesDto
import com.sekhgmainuddin.learnwithfun.data.dto.UserDetailDto
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.AddCheatFlagBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.AddScoreToAttendedQuestionBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.AttendExamBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.CreateUserBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.VerifyEmailBodyParams
import com.sekhgmainuddin.learnwithfun.data.dto.bodyParams.VerifyOTPBodyParams
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LearnWithFunApi {
    @POST("get-otp")
    suspend fun getOTP(@Body getOTP: GetOTPBodyParams) : Response<Unit>

    @POST("verify-otp")
    suspend fun verifyOTP(@Body verifyOTPBodyParams: VerifyOTPBodyParams) : Response<HashMap<String, Any>>

    @POST("user")
    suspend fun createUser(@Body createUserBodyParams: CreateUserBodyParams) : Response<CreateUserDto>

    @GET("send-mail")
    suspend fun sendMail(@Query("email") email: String) : Response<Unit>

    @POST("verify-mail")
    suspend fun verifyMail(@Body verifyEmailBodyParams: VerifyEmailBodyParams) : Response<Unit>

    @GET("user")
    suspend fun getUserDetails() : Response<UserDetailDto>

    @GET("course/get-recommended-courses")
    suspend fun getPopularCourses() : Response<PopularCoursesDto>

    @GET("course/")
    suspend fun getCourseDetails(@Query("id") id: String) : Response<CourseDetailDto>

    @POST("exam/attend-exam")
    suspend fun attendExam(@Body attendExamBodyParams: AttendExamBodyParams) : Response<AttendExamDto>

    @POST("exam/add-cheat-flag")
    suspend fun addCheatFlag(@Body addCheatFlagBodyParams: AddCheatFlagBodyParams) : Response<Unit>

    @POST("exam/add-score")
    suspend fun addScoreToAttendedQuestion(@Body addScoreToAttendedQuestionBodyParams: AddScoreToAttendedQuestionBodyParams) : Response<Unit>
}