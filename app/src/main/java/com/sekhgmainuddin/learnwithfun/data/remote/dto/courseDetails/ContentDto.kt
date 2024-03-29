package com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentDto(
    val _id: String,
    val desc: String,
    val likesIdList: List<String>,
    val notesPdfUrl: String?,
    val quiz: List<QuizDto>?,
    val thumbnail: String?,
    var title: String,
    val url: String?,
    val viewsIdList: List<String>,
    val weekNumber: Int,
    val contentDuration: Long
) : Parcelable