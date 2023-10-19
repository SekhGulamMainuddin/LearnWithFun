package com.sekhgmainuddin.learnwithfun.presentation.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sekhgmainuddin.learnwithfun.databinding.FragmentHomeBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment

class HomeFragment : BaseFragment(){

    private var _binding: FragmentHomeBinding?= null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentHomeBinding.inflate(inflater)
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