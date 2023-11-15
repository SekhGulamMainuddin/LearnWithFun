package com.sekhgmainuddin.learnwithfun.domain.modals

import com.sekhgmainuddin.learnwithfun.data.remote.dto.CourseDtoItem
import com.sekhgmainuddin.learnwithfun.data.remote.dto.UserDto

data class SearchItem(
    val course: CourseDtoItem? = null,
    val user: UserDto? = null
)