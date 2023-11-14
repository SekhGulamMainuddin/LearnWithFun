package com.sekhgmainuddin.learnwithfun.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PhoneDto(
    val countryCode: Int,
    val phoneNumber: Long
) : Parcelable