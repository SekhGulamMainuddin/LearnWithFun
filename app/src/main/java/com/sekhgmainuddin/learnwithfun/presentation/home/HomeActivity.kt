package com.sekhgmainuddin.learnwithfun.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.ActivityHomeBinding
import com.sekhgmainuddin.learnwithfun.domain.modals.CourseDetail

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.fragmentEnroll.courseDetail = CourseDetail(
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
}