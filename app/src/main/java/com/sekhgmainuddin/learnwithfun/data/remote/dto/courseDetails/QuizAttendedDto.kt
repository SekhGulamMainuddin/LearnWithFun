package com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizAttendedDto(
    val quizContentId: String,
    val examId: String?,
    val lastAttendedQuestionNumber: Int?,
    val quizCompleted: Boolean
) : Parcelable