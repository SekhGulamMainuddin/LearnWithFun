package com.sekhgmainuddin.learnwithfun.presentation.login

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.ActivityLoginSignUpBinding

class LoginSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_sign_up)

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        if (savedInstanceState == null) {
//            navController.navigate(R.id.homeFragment)
//        }

    }

}