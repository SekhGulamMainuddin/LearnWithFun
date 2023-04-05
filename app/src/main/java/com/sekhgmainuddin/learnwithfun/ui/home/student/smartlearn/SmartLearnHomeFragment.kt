package com.sekhgmainuddin.learnwithfun.ui.home.student.smartlearn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sekhgmainuddin.learnwithfun.databinding.FragmentSmartLearnHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SmartLearnHomeFragment : Fragment(){

    private var _binding: FragmentSmartLearnHomeBinding?= null
    private val binding: FragmentSmartLearnHomeBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSmartLearnHomeBinding.inflate(inflater)
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