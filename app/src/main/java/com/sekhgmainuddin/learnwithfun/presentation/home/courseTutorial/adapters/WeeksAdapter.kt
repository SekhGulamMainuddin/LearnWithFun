package com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.CourseWeekItemLayoutBinding

class WeeksAdapter(private val onWeekClickListener: (Int) -> Unit) :
    ListAdapter<Pair<String, Boolean>,WeeksAdapter.WeeksViewHolder>(WeeksDiffCallBack()) {

    var previousSelectedPosition = 0

    fun update(position: Int){
        val list = mutableListOf<Pair<String, Boolean>>()
        currentList.forEach {
            list.add(it)
        }
        list[previousSelectedPosition] = Pair("${previousSelectedPosition + 1}", false)
        list[position] = Pair("${position + 1}", true)
        previousSelectedPosition = position
        submitList(list)
    }

    private class WeeksDiffCallBack() : DiffUtil.ItemCallback<Pair<String, Boolean>>() {
        override fun areItemsTheSame(
            oldItem: Pair<String, Boolean>,
            newItem: Pair<String, Boolean>
        ): Boolean {
            return oldItem.first == newItem.first
        }

        override fun areContentsTheSame(
            oldItem: Pair<String, Boolean>,
            newItem: Pair<String, Boolean>
        ): Boolean {
            return oldItem == newItem
        }

    }

    inner class WeeksViewHolder(private val binding: CourseWeekItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.weekItem.setOnClickListener {
                onWeekClickListener(bindingAdapterPosition)
            }
        }

        fun bind(week : Pair<String, Boolean>) {
            binding.apply {
                weekNumber = week.first.toInt()
                isSelected = week.second
                isEnd = bindingAdapterPosition == (currentList.size-1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeksViewHolder {
        return WeeksViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.course_week_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeeksViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}