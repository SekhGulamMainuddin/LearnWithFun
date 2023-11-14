package com.sekhgmainuddin.learnwithfun.presentation.home.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.FragmentCoursesBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoursesFragment : BaseFragment(){

    private var _binding: FragmentCoursesBinding?= null
    private val binding: FragmentCoursesBinding
        get() = _binding!!

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

        if(arguments?.getBoolean("calledFromHome") == true) {
            showKeyboard(binding.searchChat)
            showSnackBar("dhasjdhakjsdhkjahs")
        }

        lifecycleScope.launch {
            showLoadingBar()
            delay(1500)
            hideLoadingBar()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }


}