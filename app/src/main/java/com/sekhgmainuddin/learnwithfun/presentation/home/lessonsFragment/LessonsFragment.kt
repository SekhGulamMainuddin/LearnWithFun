package com.sekhgmainuddin.learnwithfun.presentation.home.lessonsFragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.enums.LessonType
import com.sekhgmainuddin.learnwithfun.databinding.FragmentLessonsBinding
import com.sekhgmainuddin.learnwithfun.domain.modals.Lesson
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.CourseContentClickListener
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.OnCourseContentClickListener
import com.sekhgmainuddin.learnwithfun.presentation.home.lessonsFragment.adapters.LessonsAdapter

class LessonsFragment : BaseFragment() {

    private var _binding: FragmentLessonsBinding? = null
    private val binding: FragmentLessonsBinding
        get() = _binding!!

    private val args: LessonsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lessons, container, false
        )
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.courseDetail = args.courseDetails

        val courseDetails = args.courseDetails
        val list = ArrayList<Lesson>()
        val videos = ArrayList<Lesson>()
        val quiz = ArrayList<Lesson>()
        val notes = ArrayList<Lesson>()
        courseDetails.contents.forEach {
            if (!it.quiz.isNullOrEmpty()) {
                quiz.add(Lesson(it, LessonType.QUIZ))
            }
            if (!it.url.isNullOrEmpty()) {
                videos.add(Lesson(it, LessonType.VIDEO))
            }
            if (!it.notesPdfUrl.isNullOrEmpty()) {
                notes.add(Lesson(it, LessonType.NOTES))
            }
        }
        if (videos.isNotEmpty()) {
            list.add(Lesson(null, LessonType.VIDEO_LABEL))
            list.addAll(videos)
        }
        if (quiz.isNotEmpty()) {
            list.add(Lesson(null, LessonType.QUIZ_LABEL))
            quiz.forEachIndexed { i, it ->
                it.content?.title = "${context?.getString(R.string.quiz)} ${i+1} - ${it.content?.title}"
                list.add(it)
            }
        }
        if (notes.isNotEmpty()) {
            list.add(Lesson(null, LessonType.NOTES_LABEL))
            notes.forEachIndexed { i, it ->
                it.content?.title = "${context?.getString(R.string.notes)} ${i+1} - ${it.content?.title}"
                list.add(it)
            }
        }

        val lessonsAdapter = LessonsAdapter(
            list,
            requireContext(),
            object : OnCourseContentClickListener {
                override fun playVideo(contentPosition: Int) {
                    showSnackBar(R.string.please_enroll_first)
                }

                override fun attendQuiz(contentPosition: Int) {
                    showSnackBar(R.string.please_enroll_first)
                }

                override fun downloadNotes(contentPosition: Int) {
                    showSnackBar(R.string.please_enroll_first)
                }

            }
        )

        val divider = DividerItemDecoration(requireContext(), LinearLayout.VERTICAL)
        divider.setDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.vertical_10_dp_space_divider
            )!!
        )
        binding.contentsRecyclerView.addItemDecoration(divider)
        binding.contentsRecyclerView.adapter = lessonsAdapter
        binding.backButton.setOnClickListener {
            pressBack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}