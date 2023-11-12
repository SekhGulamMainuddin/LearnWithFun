package com.sekhgmainuddin.learnwithfun.data.dto.courseDetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OptionDto(
    val _id: String,
    val option: String,
    val optionType: String
) : Parcelable