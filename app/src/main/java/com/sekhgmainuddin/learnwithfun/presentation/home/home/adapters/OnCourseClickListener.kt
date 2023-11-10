package com.sekhgmainuddin.learnwithfun.presentation.home.home.adapters

interface OnCourseClickListener {
    fun onEnrolledCourseClicked(courseId: String)
    fun onCourseClicked(courseId: String)
    fun onSeeAllCoursesClicked()
}