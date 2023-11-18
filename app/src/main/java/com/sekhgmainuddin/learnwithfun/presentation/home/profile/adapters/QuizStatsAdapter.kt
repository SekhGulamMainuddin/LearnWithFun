package com.sekhgmainuddin.learnwithfun.presentation.home.profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.dto.quizStats.QuizAttendedDto
import com.sekhgmainuddin.learnwithfun.databinding.QuizStatusCvBinding

class QuizStatsAdapter(val onStatsClicked: (Int) -> Unit) :
    ListAdapter<QuizAttendedDto, QuizStatsAdapter.QuizStatsViewHolder>(QuizStatsDiff()) {

    private class QuizStatsDiff : DiffUtil.ItemCallback<QuizAttendedDto>() {
        override fun areItemsTheSame(oldItem: QuizAttendedDto, newItem: QuizAttendedDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: QuizAttendedDto,
            newItem: QuizAttendedDto
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizStatsViewHolder {
        return QuizStatsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.quiz_status_cv,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuizStatsViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    inner class QuizStatsViewHolder(private val binding: QuizStatusCvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.attendQuizButton.setOnClickListener {
                onStatsClicked(bindingAdapterPosition)
            }
        }

        fun bind(quiz: QuizAttendedDto) {
            binding.quiz = quiz
        }
    }

}