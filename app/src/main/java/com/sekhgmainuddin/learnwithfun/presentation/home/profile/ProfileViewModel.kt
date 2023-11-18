package com.sekhgmainuddin.learnwithfun.presentation.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.data.remote.dto.quizStats.QuizStatsDto
import com.sekhgmainuddin.learnwithfun.domain.use_case.profile.GetQuizStatsUseCase
import com.sekhgmainuddin.learnwithfun.presentation.home.profile.uiStates.QuizStatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getQuizStatsUseCase: GetQuizStatsUseCase
) : ViewModel() {

    private var _quizStatsState = MutableStateFlow<QuizStatsState>(QuizStatsState.Initial)
    val quizStatsState: StateFlow<QuizStatsState>
        get() = _quizStatsState

    val quizStats = MutableStateFlow<QuizStatsDto?>(null)

    fun getQuizStatsData() = viewModelScope.launch(Dispatchers.IO) {
        getQuizStatsUseCase().collect {
            when (it) {
                is NetworkResult.Success -> {
                    quizStats.value = it.data!!
                    _quizStatsState.value = QuizStatsState.Success(it.data!!)
                }

                is NetworkResult.Loading -> {
                    _quizStatsState.value = QuizStatsState.Loading
                }

                is NetworkResult.Error -> {
                    _quizStatsState.value = QuizStatsState.Error(
                        it.message,
                        it.strResMessage ?: R.string.default_error_message
                    )
                }
            }
        }
    }

}