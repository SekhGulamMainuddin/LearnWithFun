package com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AttendExamBodyParams
import com.sekhgmainuddin.learnwithfun.databinding.FragmentAttendExamDialogBinding
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.uiStates.AttendQuizState
import com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse.CourseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AttendExamDialogFragment : DialogFragment() {

    private var _binding: FragmentAttendExamDialogBinding? = null
    private val binding: FragmentAttendExamDialogBinding
        get() = _binding!!

    private val viewModel by activityViewModels<CourseViewModel>()
    private val args: AttendExamDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_attend_exam_dialog, container, false
        )
        if (arguments != null) {
            AttendExamDialogFragmentArgs.fromBundle(requireArguments())
        }
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setFreshQuiz()

        val courseDetailDto = args.course
        val contentPosition = args.contentPosition

        var maxScore = 0
        courseDetailDto.contents[contentPosition].quiz?.forEach {
            maxScore += it.marks
        }

        binding.apply {
            viewModel = this@AttendExamDialogFragment.viewModel
            lifecycleOwner = viewLifecycleOwner

            cancelButton.setOnClickListener {
                dismiss()
            }
            attendQuizButton.setOnClickListener {
                if (courseDetailDto.courseCoverage?.quizAttended?.containsKey(courseDetailDto.contents[contentPosition]._id) == true) {
                    this@AttendExamDialogFragment.viewModel.examIdAlreadyPresent(
                        courseDetailDto.courseCoverage.quizAttended[courseDetailDto.contents[contentPosition]._id]!!.examId!!
                    )
                } else {
                    this@AttendExamDialogFragment.viewModel.attendQuiz(
                        AttendExamBodyParams(
                            courseDetailDto._id,
                            courseDetailDto.contents[contentPosition]._id,
                            maxScore
                        )
                    )
                }
            }
        }
        bindObservers()
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.attendQuiz.collect {
                when (it) {
                    AttendQuizState.Initial -> {}
                    AttendQuizState.Loading -> {}
                    is AttendQuizState.Success -> {
                        delay(1000)
                        dismiss()
                    }

                    is AttendQuizState.Error -> {
                        if (it.message.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                getString(it.messageRes),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        delay(1000)
                        dismiss()
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}