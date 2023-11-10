package com.sekhgmainuddin.learnwithfun.presentation.home.home.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.dto.EnrolledCourse
import com.sekhgmainuddin.learnwithfun.databinding.EnrolledCourseProgressRvBinding
import com.sekhgmainuddin.learnwithfun.databinding.EnrolledCoursesLabelRvBinding
import com.sekhgmainuddin.learnwithfun.databinding.PopularCoursesHorizontalRvBinding
import com.sekhgmainuddin.learnwithfun.domain.modals.HomeViewContent

const val POPULAR_COURSES = 0
const val ENROLLED_COURSES_LABEL = 1
const val ENROLLED_COURSES = 2

class HomeScreenRVAdapter : ListAdapter<HomeViewContent, RecyclerView.ViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<HomeViewContent>() {
        override fun areItemsTheSame(
            oldItem: HomeViewContent,
            newItem: HomeViewContent
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HomeViewContent,
            newItem: HomeViewContent
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            POPULAR_COURSES ->
                PopularCoursesViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.popular_courses_horizontal_rv,
                        parent,
                        false
                    )
                )

            ENROLLED_COURSES_LABEL ->
                LabelViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.enrolled_courses_label_rv,
                        parent,
                        false
                    )
                )

            else ->
                EnrolledProgressViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.enrolled_course_progress_rv,
                        parent,
                        false
                    )
                )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        when (position) {
            0 -> (holder as PopularCoursesViewHolder).bind(item)
            1 -> (holder as LabelViewHolder)
            else -> (holder as EnrolledProgressViewHolder).bind(item.enrolledCourseProgress!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                POPULAR_COURSES
            }

            1 -> {
                ENROLLED_COURSES_LABEL
            }

            else -> {
                ENROLLED_COURSES
            }
        }
    }

    private inner class PopularCoursesViewHolder(val binding: PopularCoursesHorizontalRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var popularCoursesAdapter: PopularCoursesAdapter? = null
        fun bind(content: HomeViewContent) {
            if (binding.coursesRecyclerView.adapter == null) {
                popularCoursesAdapter = PopularCoursesAdapter()
                binding.apply {
                    coursesRecyclerView.adapter = popularCoursesAdapter
                    val divider = DividerItemDecoration(
                        coursesRecyclerView.context,
                        HORIZONTAL
                    )
                    divider.setDrawable(
                        ContextCompat.getDrawable(
                            coursesRecyclerView.context,
                            R.drawable.popular_course_item_divider
                        )!!
                    )
                    coursesRecyclerView.addItemDecoration(
                        divider
                    )
                }
            }
            Log.d("PopularCourses", "bind: $content")
            popularCoursesAdapter?.submitList(content.popularCourse)
        }
    }

    private inner class LabelViewHolder(binding: EnrolledCoursesLabelRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class EnrolledProgressViewHolder(val binding: EnrolledCourseProgressRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(enrolledCourse: EnrolledCourse) {
            binding.enrolledCourse = enrolledCourse
        }
    }

}