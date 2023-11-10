package com.sekhgmainuddin.learnwithfun.presentation.home.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.dto.PopularCourse
import com.sekhgmainuddin.learnwithfun.databinding.CourseItemBinding

class PopularCoursesAdapter :
    ListAdapter<PopularCourse, PopularCoursesAdapter.PopularCourseViewHolder>(DiffCallback()) {

    inner class PopularCourseViewHolder(val binding: CourseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: PopularCourse) {
            binding.course = course
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<PopularCourse>() {
        override fun areItemsTheSame(oldItem: PopularCourse, newItem: PopularCourse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PopularCourse, newItem: PopularCourse): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularCourseViewHolder {
        return PopularCourseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.course_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularCourseViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.binding.executePendingBindings()
    }


}