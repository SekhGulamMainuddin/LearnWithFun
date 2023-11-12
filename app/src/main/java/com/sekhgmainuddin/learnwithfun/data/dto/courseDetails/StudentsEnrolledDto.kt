package com.sekhgmainuddin.learnwithfun.data.dto.courseDetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentsEnrolledDto(
    val studentsId: List<String>,
    val totalCount: Int
) : Parcelable