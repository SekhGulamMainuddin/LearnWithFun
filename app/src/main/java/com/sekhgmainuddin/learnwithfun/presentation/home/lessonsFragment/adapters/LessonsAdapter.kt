package com.sekhgmainuddin.learnwithfun.presentation.home.lessonsFragment.adapters

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.enums.LessonType
import com.sekhgmainuddin.learnwithfun.databinding.ContentNotesOrQuizLayoutBinding
import com.sekhgmainuddin.learnwithfun.databinding.LabelTvBinding
import com.sekhgmainuddin.learnwithfun.databinding.LessonsVideoLayoutBinding
import com.sekhgmainuddin.learnwithfun.domain.modals.Lesson
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.CourseContentClickListener
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.OnCourseContentClickListener


class LessonsAdapter(
    private val list: List<Lesson>,
    private val context: Context,
    private val onCourseContentClickListener: OnCourseContentClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIDEO = 0
    val QUIZORNOTES = 1
    val LABEL = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIDEO -> {
                LessonsVideoViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.lessons_video_layout,
                        parent,
                        false
                    )
                )
            }

            QUIZORNOTES -> {
                QuizOrNotesViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.content_notes_or_quiz_layout,
                        parent,
                        false
                    )
                )
            }

            else -> {
                LabelTVViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.label_tv,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (getItemViewType(position)) {
            VIDEO -> (holder as LessonsVideoViewHolder).bind(item)

            QUIZORNOTES -> (holder as QuizOrNotesViewHolder).bind(item)

            else -> (holder as LabelTVViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private inner class LessonsVideoViewHolder(private val binding: LessonsVideoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.clickHandler = CourseContentClickListener(
                this@LessonsVideoViewHolder,
                onCourseContentClickListener
            )
        }

        fun bind(lesson: Lesson) {
            binding.content = lesson.content
        }
    }

    private inner class QuizOrNotesViewHolder(private val binding: ContentNotesOrQuizLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.clickHandler =
                CourseContentClickListener(this@QuizOrNotesViewHolder, onCourseContentClickListener)
            binding.quizOrNotesAnimation
        }

        fun bind(lesson: Lesson) {
            binding.content = lesson.content
        }
    }

    private inner class LabelTVViewHolder(private val binding: LabelTvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var dp10Px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10F,
            context.resources.displayMetrics
        ).toInt()
        var dp20Px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            20F,
            context.resources.displayMetrics
        ).toInt()

        init {
            val params = LinearLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(dp20Px, dp20Px, 0, dp10Px)
            binding.labelCourseTV.layoutParams = params
        }

        fun bind(lesson: Lesson) {
            binding.label =
                context.getString(if (lesson.type == LessonType.VIDEO_LABEL) R.string.videos else if (lesson.type == LessonType.NOTES_LABEL) R.string.notes else R.string.quiz)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return when (item.type) {
            LessonType.VIDEO_LABEL -> LABEL
            LessonType.QUIZ_LABEL -> LABEL
            LessonType.NOTES_LABEL -> LABEL
            LessonType.VIDEO -> VIDEO
            LessonType.QUIZ -> QUIZORNOTES
            LessonType.NOTES -> QUIZORNOTES
        }
    }

}