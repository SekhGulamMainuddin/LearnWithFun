package com.sekhgmainuddin.learnwithfun.presentation.login

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.ActivityLoginSignUpBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity

class LoginSignUpActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_sign_up)
    }
}