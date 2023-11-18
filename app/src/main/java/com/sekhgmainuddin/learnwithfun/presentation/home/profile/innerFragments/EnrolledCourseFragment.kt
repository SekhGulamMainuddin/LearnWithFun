package com.sekhgmainuddin.learnwithfun.presentation.home.profile.innerFragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.dto.EnrolledCourseDto
import com.sekhgmainuddin.learnwithfun.databinding.FragmentEnrolledCourseBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.home.HomeViewModel
import com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates.GetUserDetailsState
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.ProfileFragmentDirections
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.adapters.EnrolledCoursesAdapter
import kotlinx.coroutines.launch

class EnrolledCourseFragment : BaseFragment() {

    private var _binding: FragmentEnrolledCourseBinding? = null
    private val binding: FragmentEnrolledCourseBinding
        get() = _binding!!

    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var enrolledCoursesAdapter: EnrolledCoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_enrolled_course, container, false
        )
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerClickListenersAndAdapters()
        bindObservers()
    }

    private fun registerClickListenersAndAdapters() {
        enrolledCoursesAdapter = EnrolledCoursesAdapter {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToCourseTutorialFragment(
                    it
                )
            )
        }
        binding.enrolledCourseRV.adapter = enrolledCoursesAdapter
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userDetails.collect {
                    hideProgressBar()
                    when (it) {
                        GetUserDetailsState.Initial -> {}
                        is GetUserDetailsState.Success -> {
                            val enrolledCourses = ArrayList<EnrolledCourseDto>()
                            it.content.forEach { c ->
                                if (c.enrolledCourseProgress != null) {
                                    enrolledCourses.add(c.enrolledCourseProgress)
                                }
                            }
                            enrolledCoursesAdapter.submitList(enrolledCourses)
                        }

                        GetUserDetailsState.Loading -> showProgressBar()
                        is GetUserDetailsState.Error -> {
                            if (it.message.isEmpty()) {
                                showSnackBar(getString(it.messageRes))
                            } else {
                                showSnackBar(it.message)
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