package com.sekhgmainuddin.learnwithfun.presentation.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

        val tabs = arrayListOf(
            getString(R.string.enrolled_courses),
            getString(R.string.quiz_stats),
            getString(R.string.activity)
        )

        TabLayoutMediator(binding.profileTabLayout, binding.profileViewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}