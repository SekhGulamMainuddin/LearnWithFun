package com.sekhgmainuddin.learnwithfun.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.sekhgmainuddin.learnwithfun.databinding.ActivitySplashScreenBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity
import com.sekhgmainuddin.learnwithfun.presentation.login.LoginActivity
import com.sekhgmainuddin.learnwithfun.presentation.login.LoginSignUpViewModel
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.slideVisibility
import com.sekhgmainuddin.learnwithfun.presentation.course_video.CourseVideoActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel by viewModels<LoginSignUpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.learnFunTV.slideVisibility(true, 1000)
        },1000)

        Handler(Looper.getMainLooper()).postDelayed({
//            if (viewModel.currentUser != null)
//                startActivity(Intent(this, MainActivity::class.java))
//            else
                startActivity(Intent(this, CourseVideoActivity::class.java))
            finish()
        }, 2500)
    }
}