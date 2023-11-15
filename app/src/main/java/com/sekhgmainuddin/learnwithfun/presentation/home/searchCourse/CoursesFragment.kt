package com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.SearchCoursesAndMentorBodyParams
import com.sekhgmainuddin.learnwithfun.databinding.FragmentCoursesBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse.adapters.SearchCoursesAndMentorsAdapter
import com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse.uiStates.CoursesState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CoursesFragment : BaseFragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding: FragmentCoursesBinding
        get() = _binding!!

    private val viewModel by viewModels<CourseViewModel>()
    private lateinit var searchCourseAndMentorAdapter: SearchCoursesAndMentorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_courses, container, false
        )
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        registerClickListenersAndAdapters()
        bindObservers()
    }

    private fun registerClickListenersAndAdapters() {
        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        this@CoursesFragment.viewModel.getCoursesAndMentors(
                            SearchCoursesAndMentorBodyParams(
                                0,
                                arrayListOf(),
                                it
                            )
                        )
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })
            searchCourseAndMentorAdapter = SearchCoursesAndMentorsAdapter(
                onCourseEnrollClicked = {

                },
                onCourseExploreClicked = {

                },
            )
            val divider = DividerItemDecoration(context, LinearLayout.VERTICAL)
            divider.setDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.vertical_20_dp_space_divider
                )!!
            )
            searchCoursesAndMentorsRV.addItemDecoration(divider)
            searchCoursesAndMentorsRV.adapter = searchCourseAndMentorAdapter
        }
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coursesSearchState.collect {
                    when (it) {
                        CoursesState.Initial -> {}
                        is CoursesState.Loaded -> {
                            searchCourseAndMentorAdapter.submitList(it.list)
                            binding.searchCoursesAndMentorsRV.scheduleLayoutAnimation()
                        }

                        CoursesState.Loading -> {}
                        is CoursesState.Error -> {
                            if (it.message.isEmpty()) {
                                showSnackBar(it.messageRes)
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