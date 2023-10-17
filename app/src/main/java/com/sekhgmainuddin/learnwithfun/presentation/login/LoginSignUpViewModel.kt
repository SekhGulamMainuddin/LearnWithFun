package com.sekhgmainuddin.learnwithfun.presentation.login

import androidx.lifecycle.ViewModel
import com.sekhgmainuddin.learnwithfun.domain.repository.LoginSignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginSignUpViewModel @Inject constructor(
    private val repository: LoginSignUpRepository
) : ViewModel() {



}