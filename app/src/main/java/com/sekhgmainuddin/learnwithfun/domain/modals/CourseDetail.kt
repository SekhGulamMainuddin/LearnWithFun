package com.sekhgmainuddin.learnwithfun.domain.modals

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseDetail(
    val courseId: String,
    val courseName: String,
    val courseDesc: String,
    val courseThumbnail: String,
    val courseTeaser: String?,
    val tutorId: String,
    val tutorName: String,
    val totalLessons: Int,
    val ratings: Double,
    val totalNumberEnrolledCandidates: Long
) : Parcelable