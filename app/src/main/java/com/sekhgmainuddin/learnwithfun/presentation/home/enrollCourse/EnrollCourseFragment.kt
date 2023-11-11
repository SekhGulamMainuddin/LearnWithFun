package com.sekhgmainuddin.learnwithfun.presentation.home.enrollCourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.FragmentEnrollCourseBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.couses.CourseViewModel
import com.sekhgmainuddin.learnwithfun.presentation.home.enrollCourse.uiStates.GetCourseDetailsState
import kotlinx.coroutines.launch

class EnrollCourseFragment : BaseFragment() {

    private var _binding: FragmentEnrollCourseBinding? = null
    private val binding: FragmentEnrollCourseBinding
        get() = _binding!!

    private val viewModel by activityViewModels<CourseViewModel>()
    private val args: EnrollCourseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_enroll_course, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        if (viewModel.courseDetail.value == null || viewModel.courseDetail.value?._id != args.courseId) {
            viewModel.getCourseDetails(args.courseId)
        }
        bindObservers()
        registerClickListeners()
    }

    private fun registerClickListeners() {
        binding.apply {
            backButton.setOnClickListener {
                pressBack()
            }
        }
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.courseDetails.collect {
                    hideProgressBar()
                    when (it) {
                        GetCourseDetailsState.Initial -> {}
                        is GetCourseDetailsState.Success -> {

                        }

                        GetCourseDetailsState.Loading -> showProgressBar()
                        is GetCourseDetailsState.Error -> {
                            if (it.message.isEmpty()) {
                                showToast(it.messageRes)
                            } else {
                                showToast(it.message)
                            }
                        }
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