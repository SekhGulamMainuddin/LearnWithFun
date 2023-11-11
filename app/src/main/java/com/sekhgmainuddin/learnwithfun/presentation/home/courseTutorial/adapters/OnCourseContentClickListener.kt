package com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters

interface OnCourseContentClickListener {
    fun playVideo(contentPosition: Int)
    fun attendQuiz(contentPosition: Int)
    fun downloadNotes(contentPosition: Int)
}