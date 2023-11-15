package com.sekhgmainuddin.learnwithfun.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PhoneDto(
    val countryCode: Int,
    val phoneNumber: Long
) : Parcelable