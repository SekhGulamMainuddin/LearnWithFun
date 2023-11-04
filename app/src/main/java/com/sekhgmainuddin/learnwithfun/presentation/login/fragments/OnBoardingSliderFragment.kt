package com.sekhgmainuddin.learnwithfun.presentation.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sekhgmainuddin.learnwithfun.databinding.FragmentOnBoardingSliderBinding

class OnBoardingSliderFragment : Fragment(){

    private var _binding: FragmentOnBoardingSliderBinding?= null
    private val binding: FragmentOnBoardingSliderBinding
        get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentOnBoardingSliderBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
    
    
}