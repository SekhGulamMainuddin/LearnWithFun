package com.sekhgmainuddin.learnwithfun.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import com.sekhgmainuddin.learnwithfun.common.helper.PrefsHelper
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.slideVisibility
import com.sekhgmainuddin.learnwithfun.databinding.ActivitySplashScreenBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity
import com.sekhgmainuddin.learnwithfun.presentation.home.HomeActivity
import com.sekhgmainuddin.learnwithfun.presentation.login.LoginSignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    @Inject
    lateinit var prefsHelper: PrefsHelper

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(500)
            binding.learnFunTV.slideVisibility(true, 1000)
            delay(1250)
            prefsHelper.updateToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1NDYwMTlkYTAxMWU5NzZhM2YyZTY1ZiIsImlhdCI6MTY5OTQ3MDgyMX0.TQ24xLOcGhbCkT94Ym8PRSUSzIqhFFUlsKQnS8vUjkw")
            if (prefsHelper.getToken().isNotEmpty())
                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
            else
                startActivity(Intent(this@SplashScreenActivity, LoginSignUpActivity::class.java))
            finish()
        }
    }
}