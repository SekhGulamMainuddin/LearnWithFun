package com.sekhgmainuddin.learnwithfun.ui.home.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(private val repository: LearnRepository) : ViewModel() {

    val banners= repository.banners

    fun getBanners()= viewModelScope.launch(Dispatchers.IO){
        repository.getBanners()
    }

}