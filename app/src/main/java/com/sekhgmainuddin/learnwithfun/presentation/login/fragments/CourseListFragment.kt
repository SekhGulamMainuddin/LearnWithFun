package com.sekhgmainuddin.learnwithfun.presentation.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.FragmentCourseListBinding

class CourseListFragment : Fragment(){

    private var _binding: FragmentCourseListBinding?= null
    private val binding: FragmentCourseListBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_course_list, container, false
        )
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