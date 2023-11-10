package com.sekhgmainuddin.learnwithfun.presentation.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.FragmentHomeBinding
import com.sekhgmainuddin.learnwithfun.domain.modals.HomeViewContent
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.HomeViewModel
import com.sekhgmainuddin.learnwithfun.presentation.home.home.adapters.HomeScreenRVAdapter
import com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates.GetPopularCoursesState
import com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates.GetUserDetailsState
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var homeScreenRVAdapter: HomeScreenRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerListenersAndAdapters()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        if(viewModel.userDetail.value == null){
            viewModel.getUserDetails()
            viewModel.getPopularCourses()
        }
        bindObserver()
    }

    private fun registerListenersAndAdapters() {
        homeScreenRVAdapter = HomeScreenRVAdapter()
        binding.homeScreenRv.adapter = homeScreenRVAdapter
        homeScreenRVAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0){
                    binding.homeScreenRv.layoutManager?.scrollToPosition(0)
                }
            }
        })
    }

    private fun bindObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.userDetails.collect {
                        hideProgressBar()
                        when (it) {
                            GetUserDetailsState.Initial -> {}
                            is GetUserDetailsState.Success -> {
                                homeScreenRVAdapter.submitList(it.homeViewListContent)
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

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.popularCourses.collect {
                        when (it) {
                            GetPopularCoursesState.Initial -> {}
                            is GetPopularCoursesState.Success -> {
                                homeScreenRVAdapter.submitList(it.content)
                            }
                            GetPopularCoursesState.Loading -> {}
                            is GetPopularCoursesState.Error -> {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}