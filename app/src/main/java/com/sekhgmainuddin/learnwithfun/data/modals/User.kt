package com.sekhgmainuddin.learnwithfun.data.modals

@kotlinx.serialization.Serializable
data class User(
    val name: String = "",
    var userId: String= "",
    val email: String = "",
    val phone: String = "",
    val imageUrl: String = "",
    val bio: String? = null,
    val interests: ArrayList<String>? = null,
    val location: String? = null,
    val activeStatus: Long = 0L,
    val friends: ArrayList<String>? = null,
    val followers: ArrayList<String>? = null,
    val following: ArrayList<String>? = null,
    val isVerified: Boolean = false,
    val isSelected: Boolean = false

) : java.io.Serializable