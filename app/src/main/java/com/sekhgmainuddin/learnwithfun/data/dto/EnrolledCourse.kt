package com.sekhgmainuddin.learnwithfun.data.dto

data class EnrolledCourse(
    val courseId: String,
    val courseName: String,
    val courseThumbnail: String,
    val instructorName: String,
    val courseCoverage: Int,
    val courseLength: Int,
    var courseCoverageProgress: Int?
)