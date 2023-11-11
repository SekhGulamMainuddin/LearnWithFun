package com.sekhgmainuddin.learnwithfun.data.dto.courseDetails

data class CourseCoverageDto(
    val __v: Int,
    val _id: String,
    val contentCovered: List<String>,
    val courseId: String,
    val learnerId: String,
    val quizAttended: HashMap<String, QuizAttendedDto>
)