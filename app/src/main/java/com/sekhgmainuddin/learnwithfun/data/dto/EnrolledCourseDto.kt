package com.sekhgmainuddin.learnwithfun.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EnrolledCourseDto(
    val courseId: String,
    val courseName: String,
    val courseThumbnail: String,
    val instructorName: String,
    val courseCoverage: Int,
    val courseLength: Int,
    var courseCoverageProgress: Int?
) : Parcelable