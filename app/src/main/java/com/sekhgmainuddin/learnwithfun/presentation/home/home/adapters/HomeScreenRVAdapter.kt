package com.sekhgmainuddin.learnwithfun.presentation.home.home.adapters

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
import com.sekhgmainuddin.learnwithfun.data.dto.EnrolledCourseDto
import com.sekhgmainuddin.learnwithfun.databinding.EnrolledCourseProgressRvBinding
import com.sekhgmainuddin.learnwithfun.databinding.EnrolledCoursesLabelRvBinding
import com.sekhgmainuddin.learnwithfun.databinding.PopularCoursesHorizontalRvBinding
import com.sekhgmainuddin.learnwithfun.domain.modals.HomeViewContent

class HomeScreenRVAdapter(
    private val onCourseClickListener: OnCourseClickListener
) : ListAdapter<HomeViewContent, RecyclerView.ViewHolder>(DiffCallback()) {

    private val POPULAR_COURSES = 0
    private val ENROLLED_COURSES_LABEL = 1
    private val ENROLLED_COURSES = 2

    private class DiffCallback : DiffUtil.ItemCallback<HomeViewContent>() {
        override fun areItemsTheSame(
            oldItem: HomeViewContent,
            newItem: HomeViewContent
        ): Boolean {
            return oldItem.enrolledCourseProgress == newItem.enrolledCourseProgress && oldItem.popularCourse == newItem.popularCourse
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
                    ),
                    onCourseClickListener
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
            0 -> {
                val popularCoursesHolder = holder as PopularCoursesViewHolder
                popularCoursesHolder.bind(item)
                popularCoursesHolder.binding.seeAllButton.setOnClickListener {
                    onCourseClickListener.onSeeAllCoursesClicked()
                }
            }

            1 -> (holder as LabelViewHolder)
            else -> {
                val enrolledCourseHolder = holder as EnrolledProgressViewHolder
                enrolledCourseHolder.bind(item.enrolledCourseProgress!!)
                enrolledCourseHolder.binding.playCurrentCourseButton.setOnClickListener {
                    onCourseClickListener.onEnrolledCourseClicked(item.enrolledCourseProgress.courseId)
                }
            }
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

    private inner class PopularCoursesViewHolder(
        val binding: PopularCoursesHorizontalRvBinding,
        val courseClickListener: OnCourseClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var popularCoursesAdapter: PopularCoursesAdapter? = null
        fun bind(content: HomeViewContent) {
            if (binding.coursesRecyclerView.adapter == null) {
                popularCoursesAdapter = PopularCoursesAdapter(courseClickListener)
                binding.apply {
                    coursesRecyclerView.adapter = popularCoursesAdapter
                    val divider = DividerItemDecoration(
                        coursesRecyclerView.context,
                        HORIZONTAL
                    )
                    divider.setDrawable(
                        ContextCompat.getDrawable(
                            coursesRecyclerView.context,
                            R.drawable.horizontal_20_dp_space_divider
                        )!!
                    )
                    coursesRecyclerView.addItemDecoration(
                        divider
                    )
                }
            }
            popularCoursesAdapter?.submitList(content.popularCourse)
        }
    }

    private inner class LabelViewHolder(binding: EnrolledCoursesLabelRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class EnrolledProgressViewHolder(
        val binding: EnrolledCourseProgressRvBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(enrolledCourse: EnrolledCourseDto) {
            binding.enrolledCourse = enrolledCourse
        }
    }

}