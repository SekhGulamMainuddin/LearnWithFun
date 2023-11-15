package com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.ContentDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.databinding.ContentNotesOrQuizLayoutBinding
import com.sekhgmainuddin.learnwithfun.databinding.CourseContentItemBinding
import np.com.susanthapa.curved_bottom_navigation.getColorRes

class CourseContentAdapter(private val onCourseContentClickListener: OnCourseContentClickListener) :
    ListAdapter<ContentDto, ViewHolder>(ContentDiffCallback()) {

    val CONTENT_WITH_VIDEO_QUIZ_AND_NOTES = 0
    val CONTENT_WITH_VIDEO_AND_NOTES = 1
    val CONTENT_WITH_VIDEO_AND_QUIZ = 2
    val CONTENT_WITH_VIDEO_ONLY = 3
    val CONTENT_WITH_QUIZ_ONLY = 4
    val CONTENT_WITH_NOTES_ONLY = 5

    private var courseDetails: CourseDetailDto? = null

    fun updateCourseDetails(courseDetails: CourseDetailDto) {
        this.courseDetails = courseDetails
    }

    private class ContentDiffCallback : DiffUtil.ItemCallback<ContentDto>() {
        override fun areItemsTheSame(oldItem: ContentDto, newItem: ContentDto): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: ContentDto, newItem: ContentDto): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            CONTENT_WITH_VIDEO_QUIZ_AND_NOTES -> ContentWithVideoNotesAndQuiz(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.course_content_item,
                    parent,
                    false
                )
            )

            CONTENT_WITH_VIDEO_AND_NOTES -> ContentWithVideoAndNotes(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.course_content_item,
                    parent,
                    false
                )
            )

            CONTENT_WITH_VIDEO_AND_QUIZ -> ContentWithVideoAndQuiz(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.course_content_item,
                    parent,
                    false
                )
            )

            CONTENT_WITH_VIDEO_ONLY -> ContentWithOnlyVideo(
                DataBindingUtil.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), R.layout.course_content_item, parent, false
                )
            )

            CONTENT_WITH_NOTES_ONLY -> ContentWithNotesOnly(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.content_notes_or_quiz_layout,
                    parent,
                    false
                )
            )

            else -> ContentWithQuizOnly(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.content_notes_or_quiz_layout,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        when (getItemViewType(position)) {
            CONTENT_WITH_VIDEO_QUIZ_AND_NOTES -> (holder as ContentWithVideoNotesAndQuiz).bind(item)
            CONTENT_WITH_VIDEO_AND_NOTES -> (holder as ContentWithVideoAndNotes).bind(item)
            CONTENT_WITH_VIDEO_AND_QUIZ -> (holder as ContentWithVideoAndQuiz).bind(item)
            CONTENT_WITH_VIDEO_ONLY -> (holder as ContentWithOnlyVideo).bind(item)
            CONTENT_WITH_NOTES_ONLY -> (holder as ContentWithNotesOnly).bind(item)
            else -> (holder as ContentWithQuizOnly).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return if (item.url != null && !item.quiz.isNullOrEmpty() && item.notesPdfUrl != null) {
            CONTENT_WITH_VIDEO_QUIZ_AND_NOTES
        } else if (item.url != null && !item.quiz.isNullOrEmpty()) {
            CONTENT_WITH_VIDEO_AND_QUIZ
        } else if (item.url != null && item.notesPdfUrl != null) {
            CONTENT_WITH_VIDEO_AND_NOTES
        } else if (item.url == null && item.quiz != null) {
            CONTENT_WITH_QUIZ_ONLY
        } else if (item.quiz.isNullOrEmpty()) {
            CONTENT_WITH_VIDEO_ONLY
        } else {
            CONTENT_WITH_NOTES_ONLY
        }
    }

    private inner class ContentWithOnlyVideo(
        private val binding: CourseContentItemBinding
    ) :
        ViewHolder(binding.root) {
        init {
            binding.apply {
                playQuiz.isVisible = false
                downloadNotesButton.isVisible = false
                clickHandler = CourseContentClickListener(
                    this@ContentWithOnlyVideo,
                    onCourseContentClickListener
                )
            }
        }

        fun bind(content: ContentDto) {
            binding.content = content
        }
    }

    private inner class ContentWithVideoNotesAndQuiz(
        private val binding: CourseContentItemBinding
    ) :
        ViewHolder(binding.root) {
        init {
            binding.clickHandler = CourseContentClickListener(this, onCourseContentClickListener)
        }

        fun bind(content: ContentDto) {
            binding.apply {
                binding.content = content
                playQuiz.setQuizButtonDrawableAndColor(content)
            }
        }
    }

    private inner class ContentWithVideoAndNotes(
        private val binding: CourseContentItemBinding
    ) :
        ViewHolder(binding.root) {
        init {
            binding.apply {
                playQuiz.isVisible = false
                clickHandler = CourseContentClickListener(
                    this@ContentWithVideoAndNotes,
                    onCourseContentClickListener
                )
            }
        }

        fun bind(content: ContentDto) {
            binding.content = content
        }
    }

    private inner class ContentWithVideoAndQuiz(
        private val binding: CourseContentItemBinding
    ) :
        ViewHolder(binding.root) {
        init {
            binding.apply {
                downloadNotesButton.isVisible = false
                clickHandler = CourseContentClickListener(
                    this@ContentWithVideoAndQuiz,
                    onCourseContentClickListener
                )
            }
        }

        fun bind(content: ContentDto) {
            binding.apply {
                binding.content = content
                this.content = content
                playQuiz.setQuizButtonDrawableAndColor(content)
            }
        }
    }

    private inner class ContentWithQuizOnly(
        private val binding: ContentNotesOrQuizLayoutBinding
    ) :
        ViewHolder(binding.root) {
        init {
            binding.apply {
                clickHandler = CourseContentClickListener(
                    this@ContentWithQuizOnly,
                    onCourseContentClickListener
                )
            }
        }

        fun bind(content: ContentDto) {
            var quizButtonDrawable: Int = R.drawable.baseline_navigate_next_24
            var quizButtonColor: Int = R.color.science_blue
            if (courseDetails?.courseCoverage?.quizAttended?.containsKey(content._id) == true) {
                if (courseDetails!!.courseCoverage!!.quizAttended[content._id]!!.quizCompleted) {
                    quizButtonDrawable = R.drawable.baseline_done_24
                    quizButtonColor = R.color.paris_green
                } else {
                    quizButtonColor = R.color.orange
                }
            }
            binding.apply {
                this.content = content
                attendOrViewButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        attendOrViewButton.context,
                        quizButtonDrawable
                    )
                )
                attendOrViewButton.setBackgroundColor(
                    attendOrViewButton.context.getColorRes(
                        quizButtonColor
                    )
                )
            }
        }
    }

    private inner class ContentWithNotesOnly(
        private val binding: ContentNotesOrQuizLayoutBinding
    ) :
        ViewHolder(binding.root) {
        init {
            binding.apply {
                clickHandler = CourseContentClickListener(
                    this@ContentWithNotesOnly,
                    onCourseContentClickListener
                )
            }
        }

        fun bind(content: ContentDto) {
            binding.content = content
        }

    }

    fun Button.setQuizButtonDrawableAndColor(content: ContentDto) {
        var quizButtonDrawable: Int = R.drawable.attend_quiz_drawable
        var quizButtonColor: Int = R.color.science_blue
        if (courseDetails?.courseCoverage?.quizAttended?.containsKey(content._id) == true) {
            if (courseDetails!!.courseCoverage!!.quizAttended[content._id]!!.quizCompleted) {
                quizButtonDrawable = R.drawable.baseline_done_24
                quizButtonColor = R.color.paris_green
            } else {
                quizButtonColor = R.color.orange
            }
        }
        this.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this.context,
                quizButtonDrawable
            ), null, null, null
        )
        this.setBackgroundColor(this.context.getColorRes(quizButtonColor))
    }

}