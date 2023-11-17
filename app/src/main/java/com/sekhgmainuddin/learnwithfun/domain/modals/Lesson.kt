package com.sekhgmainuddin.learnwithfun.domain.modals

import com.sekhgmainuddin.learnwithfun.common.enums.LessonType
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.ContentDto

data class Lesson (
    val content: ContentDto?,
    val type: LessonType,
)