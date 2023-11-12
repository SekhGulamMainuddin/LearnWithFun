package com.sekhgmainuddin.learnwithfun.data.dto.courseDetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseCoverageDto(
    val __v: Int,
    val _id: String,
    val contentCovered: List<String>,
    val courseId: String,
    val learnerId: String,
    val quizAttended: HashMap<String, QuizAttendedDto>
) : Parcelable