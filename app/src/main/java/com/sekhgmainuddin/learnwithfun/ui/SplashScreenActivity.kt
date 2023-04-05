package com.sekhgmainuddin.learnwithfun.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.sekhgmainuddin.learnwithfun.databinding.ActivitySplashScreenBinding
import com.sekhgmainuddin.learnwithfun.ui.home.MainActivity
import com.sekhgmainuddin.learnwithfun.ui.login.LoginActivity
import com.sekhgmainuddin.learnwithfun.ui.login.LoginSignUpViewModel
import com.sekhgmainuddin.learnwithfun.utils.Utils.slideVisibility
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

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
            if (viewModel.currentUser != null)
                startActivity(Intent(this, MainActivity::class.java))
            else
                startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2500)
    }
}