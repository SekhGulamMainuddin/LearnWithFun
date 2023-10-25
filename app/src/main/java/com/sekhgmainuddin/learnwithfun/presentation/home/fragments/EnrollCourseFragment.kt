package com.sekhgmainuddin.learnwithfun.presentation.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sekhgmainuddin.learnwithfun.databinding.FragmentEnrollCourseBinding
import com.sekhgmainuddin.learnwithfun.domain.modals.CourseDetail

class EnrollCourseFragment : Fragment() {

    private var _binding: FragmentEnrollCourseBinding? = null
    private val binding: FragmentEnrollCourseBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnrollCourseBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.courseDetail = CourseDetail(
            "",
            "Figma Designing for Beginners",
            "This is a course designed for all the beginners This is a course designed for all the beginners This is a course designed for all the beginners This is a course designed for all the beginners This is a course designed for all the beginnersThis is a course designed for all the beginners",
            "",
            "",
            "",
            "Sekh Gulam Mainuddin",
            10,
            3.5,
            7,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}