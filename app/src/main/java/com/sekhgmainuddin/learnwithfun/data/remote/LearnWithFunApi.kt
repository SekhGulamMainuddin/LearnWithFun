package com.sekhgmainuddin.learnwithfun.data.remote

import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AddCheatFlagBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AddScoreToAttendedQuestionBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AttendExamBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.CreateUserBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.SearchCoursesAndMentorBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.UpdateActivityBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyEmailBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyPaymentBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.AttendExamDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.CourseAndUserSearchDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.CreateUserDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.PaymentTokenDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.PopularCoursesDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.UserDetailDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.quizStats.QuizStatsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LearnWithFunApi {
    @POST("get-otp")
    suspend fun getOTP(@Body getOTP: GetOTPBodyParams): Response<Unit>

    @POST("verify-otp")
    suspend fun verifyOTP(@Body verifyOTPBodyParams: VerifyOTPBodyParams): Response<HashMap<String, Any>>

    @POST("user")
    suspend fun createUser(@Body createUserBodyParams: CreateUserBodyParams): Response<CreateUserDto>

    @GET("send-mail")
    suspend fun sendMail(@Query("email") email: String): Response<Unit>

    @POST("verify-mail")
    suspend fun verifyMail(@Body verifyEmailBodyParams: VerifyEmailBodyParams): Response<Unit>

    @GET("user")
    suspend fun getUserDetails(): Response<UserDetailDto>

    @GET("course/get-recommended-courses")
    suspend fun getPopularCourses(): Response<PopularCoursesDto>

    @GET("course/")
    suspend fun getCourseDetails(@Query("id") id: String): Response<CourseDetailDto>

    @POST("exam/attend-exam")
    suspend fun attendExam(@Body attendExamBodyParams: AttendExamBodyParams): Response<AttendExamDto>

    @POST("exam/add-cheat-flag")
    suspend fun addCheatFlag(@Body addCheatFlagBodyParams: AddCheatFlagBodyParams): Response<Unit>

    @POST("exam/add-score")
    suspend fun addScoreToAttendedQuestion(@Body addScoreToAttendedQuestionBodyParams: AddScoreToAttendedQuestionBodyParams): Response<Unit>

    @POST("course/search-courses-mentors")
    suspend fun getCoursesAndMentorsForQuery(@Body searchCoursesAndMentorBodyParams: SearchCoursesAndMentorBodyParams): Response<CourseAndUserSearchDto>

    @GET("payment/token")
    suspend fun getPaymentToken(): Response<PaymentTokenDto>

    @POST("payment/verify-payment")
    suspend fun verifyPayment(@Body verifyPaymentBodyParams: VerifyPaymentBodyParams): Response<Unit>

    @GET("exam/exam-stats")
    suspend fun getQuizStats() : Response<QuizStatsDto>

    @POST("user/update-activity")
    suspend fun updateActivity(@Body updateActivityBodyParams: UpdateActivityBodyParams)
}