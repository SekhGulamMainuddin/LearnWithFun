package com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.CourseListItemViewBinding
import com.sekhgmainuddin.learnwithfun.databinding.MentorLayoutSearchBinding
import com.sekhgmainuddin.learnwithfun.domain.modals.SearchItem

class SearchCoursesAndMentorsAdapter(
    private val onCourseEnrollClicked: (Int) -> Unit,
    private val onCourseExploreClicked: (Int) -> Unit,
) :
    ListAdapter<SearchItem, RecyclerView.ViewHolder>(CourseItemDiffCallback()) {

    private val ITEM_MENTOR = 0
    private val ITEM_WITHOUT_DISCOUNT = 1
    private val ITEM_WITH_DISCOUNT = 2

    private class CourseItemDiffCallback : DiffUtil.ItemCallback<SearchItem>() {
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_WITHOUT_DISCOUNT -> {
                CourseListItemViewHolderWithoutDiscount(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), R.layout.course_list_item_view, parent, false
                    )
                )
            }

            ITEM_WITH_DISCOUNT -> {
                CourseListItemViewHolderWithDiscount(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), R.layout.course_list_item_view, parent, false
                    )
                )
            }

            else -> {
                MentorViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), R.layout.mentor_layout_search, parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        when (getItemViewType(position)) {
            ITEM_MENTOR -> {
                (holder as MentorViewHolder).bind(item)
            }

            ITEM_WITH_DISCOUNT -> {
                (holder as CourseListItemViewHolderWithDiscount).bind(item)
            }

            ITEM_WITHOUT_DISCOUNT -> {
                (holder as CourseListItemViewHolderWithoutDiscount).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return if (item.user != null) {
            ITEM_MENTOR
        } else if (item.course!!.discount == 0) {
            ITEM_WITHOUT_DISCOUNT
        } else {
            ITEM_WITH_DISCOUNT
        }
    }

    inner class CourseListItemViewHolderWithoutDiscount(private val binding: CourseListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                enrollButton.setOnClickListener {
                    onCourseEnrollClicked(bindingAdapterPosition)
                }
                exploreButton.setOnClickListener {
                    onCourseExploreClicked(bindingAdapterPosition)
                }
            }
        }

        fun bind(item: SearchItem) {
            binding.course = item.course
        }
    }

    inner class CourseListItemViewHolderWithDiscount(private val binding: CourseListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                enrollButton.setOnClickListener {
                    onCourseEnrollClicked(bindingAdapterPosition)
                }
                exploreButton.setOnClickListener {
                    onCourseExploreClicked(bindingAdapterPosition)
                }
            }
        }

        fun bind(item: SearchItem) {
            binding.course = item.course
        }
    }

    inner class MentorViewHolder(private val binding: MentorLayoutSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(item: SearchItem) {
            binding.user = item.user
        }

    }

}