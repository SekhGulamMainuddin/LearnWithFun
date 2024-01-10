package com.sekhgmainuddin.learnwithfun.presentation.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.FragmentProfileBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.home.HomeViewModel
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.adapters.ProfileViewPagerAdapter
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.innerFragments.ActivityFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.innerFragments.EnrolledCourseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.innerFragments.QuizStatisticsFragment

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        registerClickListeners()
        setUpProfileViewPager()
    }

    private fun registerClickListeners() {
        binding.apply {
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            notificationIcon.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToNotificationsFragment())
            }
        }
    }

    private fun setUpProfileViewPager() {
        val profileViewPagerAdapter = ProfileViewPagerAdapter(
            childFragmentManager,
            lifecycle,
            listOf(EnrolledCourseFragment(), QuizStatisticsFragment(), ActivityFragment())
        )
        binding.profileViewPager.adapter = profileViewPagerAdapter

        val tabsLabel = arrayListOf(
            getString(R.string.enrolled_courses),
            getString(R.string.quiz_stats),
            getString(R.string.activity)
        )
        val tabButton = arrayListOf(
            binding.coursesButton,
            binding.quizStatsButton,
            binding.activityButton
        )
        tabButton.forEachIndexed { i, it->
            it.setOnClickListener {
                binding.profileViewPager.setCurrentItem(i, true)
            }
        }
        binding.profileViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.viewPagerLabel.text = tabsLabel[position]
                tabButton.forEachIndexed { i, it->
                    it.isSelected = i == position
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}