package com.sekhgmainuddin.learnwithfun.presentation.quiz

import androidx.lifecycle.ViewModel
import com.sekhgmainuddin.learnwithfun.common.enums.CheatingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor() : ViewModel() {

    fun triggerExamViolation(cheatingStatus: CheatingStatus) {

    }

}