package com.sekhgmainuddin.learnwithfun.presentation.courseVideo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.ContentDto
import com.sekhgmainuddin.learnwithfun.databinding.OtherVideoContentItemBinding
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.CourseContentClickListener
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.OnCourseContentClickListener

class ContentOtherVideosListAdapter(private val onCourseContentClickListener: (Int) -> Unit) :
    ListAdapter<ContentDto, ContentOtherVideosListAdapter.OtherVideoContentViewHolder>(
        OtherVideoDiffCallback()
    ) {

    private class OtherVideoDiffCallback : DiffUtil.ItemCallback<ContentDto>() {
        override fun areItemsTheSame(oldItem: ContentDto, newItem: ContentDto): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: ContentDto, newItem: ContentDto): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherVideoContentViewHolder {
        return OtherVideoContentViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.other_video_content_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OtherVideoContentViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class OtherVideoContentViewHolder(private val binding: OtherVideoContentItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        init {
            binding.courseVideoItemLayout.setOnClickListener {
                onCourseContentClickListener(bindingAdapterPosition)
            }
        }

        fun bind(contentDto: ContentDto) {
            binding.content = contentDto
        }

    }

}