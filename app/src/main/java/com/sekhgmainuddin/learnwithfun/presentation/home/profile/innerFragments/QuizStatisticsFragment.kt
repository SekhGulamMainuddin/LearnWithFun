package com.sekhgmainuddin.learnwithfun.presentation.home.profile.innerFragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.dto.quizStats.QuizAttendedDto
import com.sekhgmainuddin.learnwithfun.databinding.FragmentQuizStatisticsBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.ProfileFragmentDirections
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.ProfileViewModel
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.adapters.QuizStatsAdapter
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.uiStates.QuizStatsState
import kotlinx.coroutines.launch

class QuizStatisticsFragment : BaseFragment() {

    private var _binding: FragmentQuizStatisticsBinding? = null
    private val binding: FragmentQuizStatisticsBinding
        get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var quizStatsAdapter: QuizStatsAdapter
    private val list = ArrayList<QuizAttendedDto>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_statistics, container, false
        )
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        registerClickListenersAndAdapters()
        viewModel.getQuizStatsData()
        bindObservers()
    }

    private fun registerClickListenersAndAdapters() {
        binding.apply {
            quizStatsAdapter = QuizStatsAdapter {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToCourseTutorialFragment(
                        list[it].courseId,
                        list[it].totalQuizAttended
                    )
                )
            }
            val divider = DividerItemDecoration(context, LinearLayout.VERTICAL)
            divider.setDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.vertical_10_dp_space_divider
                )!!
            )
            statsRecyclerView.addItemDecoration(divider)
            statsRecyclerView.adapter = quizStatsAdapter
        }
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.quizStatsState.collect {
                    when (it) {
                        QuizStatsState.Initial -> {}
                        QuizStatsState.Loading -> {}
                        is QuizStatsState.Success -> {
                            list.clear()
                            list.addAll(it.stats.quizAttendedList)
                            quizStatsAdapter.submitList(list)
                        }

                        is QuizStatsState.Error -> {
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