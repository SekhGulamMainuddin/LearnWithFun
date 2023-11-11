package com.sekhgmainuddin.learnwithfun.presentation.home.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.dto.PopularCourseDto
import com.sekhgmainuddin.learnwithfun.databinding.PopularCourseItemBinding

class PopularCoursesAdapter(
    private val courseClickListener: OnCourseClickListener
) :
    ListAdapter<PopularCourseDto, PopularCoursesAdapter.PopularCourseViewHolder>(DiffCallback()) {

    inner class PopularCourseViewHolder(
        val binding: PopularCourseItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: PopularCourseDto) {
            binding.course = course
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<PopularCourseDto>() {
        override fun areItemsTheSame(oldItem: PopularCourseDto, newItem: PopularCourseDto): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PopularCourseDto, newItem: PopularCourseDto): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularCourseViewHolder {
        return PopularCourseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.popular_course_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularCourseViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.itemView.setOnClickListener {
            courseClickListener.onCourseClicked(currentList[position]._id)
        }
        holder.binding.executePendingBindings()
    }


}