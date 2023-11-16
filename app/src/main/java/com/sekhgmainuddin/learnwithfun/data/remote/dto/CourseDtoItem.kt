package com.sekhgmainuddin.learnwithfun.data.remote.dto

import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.StudentsEnrolledDto

data class CourseDtoItem(
    val __v: Int,
    val _id: String,
    val courseDesc: String,
    val courseName: String,
    val courseThumbnail: String,
    val discount: Double,
    val ratings: Double,
    val instructorId: String,
    val instructorName: String,
    val instructorProfilePicture: String,
    val lessons: Int,
    val likesIdList: List<String>,
    val price: Double,
    val studentsEnrolled: StudentsEnrolledDto,
    val tags: List<String>
)