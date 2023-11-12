package com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CourseContentClickListener(
    private val viewHolder: RecyclerView.ViewHolder,
    private val onCourseContentClickListener: OnCourseContentClickListener
) {
    fun playVideo(v: View?) {
        onCourseContentClickListener.playVideo(viewHolder.bindingAdapterPosition)
    }

    fun attendQuiz(v: View?) {
        onCourseContentClickListener.attendQuiz(viewHolder.bindingAdapterPosition)
    }

    fun downloadNotes(v: View?) {
        onCourseContentClickListener.downloadNotes(viewHolder.bindingAdapterPosition)
    }
}