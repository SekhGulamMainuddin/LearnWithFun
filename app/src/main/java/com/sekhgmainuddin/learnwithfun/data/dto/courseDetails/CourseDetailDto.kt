package com.sekhgmainuddin.learnwithfun.data.dto.courseDetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseDetailDto(
    val __v: Int,
    val _id: String,
    val contents: List<ContentDto>,
    val courseDesc: String,
    val courseName: String,
    val courseThumbnail: String,
    val instructorId: String,
    val price: Double,
    val studentsEnrolled: StudentsEnrolledDto,
    val tags: List<String>,
    val instructorName: String,
    val weekMap: HashMap<String, Int>,
    val courseCoverage: CourseCoverageDto
) : Parcelable