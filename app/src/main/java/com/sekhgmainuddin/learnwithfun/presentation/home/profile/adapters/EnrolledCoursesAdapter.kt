package com.sekhgmainuddin.learnwithfun.presentation.home.profile.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.dto.EnrolledCourseDto
import com.sekhgmainuddin.learnwithfun.databinding.EnrolledCourseProgressRvBinding

class EnrolledCoursesAdapter(val context: Context, val onCourseClicked: (String) -> Unit) :
    ListAdapter<EnrolledCourseDto, EnrolledCoursesAdapter.EnrolledCourseViewHolder>(
        EnrolledCourseDiffCallback()
    ) {

    private class EnrolledCourseDiffCallback() : DiffUtil.ItemCallback<EnrolledCourseDto>() {
        override fun areItemsTheSame(
            oldItem: EnrolledCourseDto,
            newItem: EnrolledCourseDto
        ): Boolean {
            return oldItem.courseId == newItem.courseId
        }

        override fun areContentsTheSame(
            oldItem: EnrolledCourseDto,
            newItem: EnrolledCourseDto
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrolledCourseViewHolder {
        return EnrolledCourseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.enrolled_course_progress_rv,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EnrolledCourseViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
        holder.binding.playCurrentCourseButton.setOnClickListener {
            onCourseClicked(item.courseId)
        }
    }

    inner class EnrolledCourseViewHolder(
        val binding: EnrolledCourseProgressRvBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        var dp10Px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10F,
            context.resources.displayMetrics
        ).toInt()

        init {
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(dp10Px, 0, dp10Px, dp10Px)
            binding.courseProgressCV.layoutParams = layoutParams
        }

        fun bind(enrolledCourse: EnrolledCourseDto) {
            binding.enrolledCourse = enrolledCourse
        }
    }
}