package com.sekhgmainuddin.learnwithfun.presentation.exam

import android.os.Bundle
import com.sekhgmainuddin.learnwithfun.databinding.ActivityExamBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity

class ExamActivity : BaseActivity() {

    private lateinit var binding: ActivityExamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}