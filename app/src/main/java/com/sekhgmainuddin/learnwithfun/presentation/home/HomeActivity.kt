package com.sekhgmainuddin.learnwithfun.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.ActivityHomeBinding
import com.sekhgmainuddin.learnwithfun.domain.modals.CourseDetail
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

    }
}