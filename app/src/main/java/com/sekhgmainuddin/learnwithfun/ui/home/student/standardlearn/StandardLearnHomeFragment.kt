package com.sekhgmainuddin.learnwithfun.ui.home.student.standardlearn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sekhgmainuddin.learnwithfun.databinding.FragmentStandardLearnHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StandardLearnHomeFragment : Fragment() {

    private var _binding: FragmentStandardLearnHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStandardLearnHomeBinding.inflate(inflater)

        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}