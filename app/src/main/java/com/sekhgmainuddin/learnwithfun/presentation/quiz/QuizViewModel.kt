package com.sekhgmainuddin.learnwithfun.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.enums.CheatingStatus
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.data.db.entities.CheatFlagEntity
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AddScoreToAttendedQuestionBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.UpdateActivityBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.ContentDto
import com.sekhgmainuddin.learnwithfun.domain.modals.Quiz
import com.sekhgmainuddin.learnwithfun.domain.use_case.activity.UpdateUserActivityUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.quiz.AddCorrectAnsScoreUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.quiz.TriggerExamViolationUseCase
import com.sekhgmainuddin.learnwithfun.presentation.quiz.uiStates.QuizState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val addCorrectAnsScoreUseCase: AddCorrectAnsScoreUseCase,
    private val triggerExamViolationUseCase: TriggerExamViolationUseCase,
    private val updateUserActivityUseCase: UpdateUserActivityUseCase
) : ViewModel() {

    val currentQuiz = MutableStateFlow<Quiz?>(null)
    val questionNumber = MutableStateFlow<Int>(1)
    val questionsList = ArrayList<Quiz>()
    var examId = ""
    var courseId = ""
    private var currentQuestion = 0
    val quizStates = MutableStateFlow<QuizState>(QuizState.Initial)
    val cheatingStates = MutableSharedFlow<CheatingStatus>()

    fun setQuestions(contentDto: ContentDto) {
        questionsList.clear()
        contentDto.quiz?.forEach {
            questionsList.add(Quiz(it))
        }
        currentQuestion = 0
        currentQuiz.value = questionsList[currentQuestion]
    }

    fun checkAnswer(option: Int) {
        val quiz = questionsList[currentQuestion]
        questionsList[currentQuestion] = Quiz(quiz.quiz, option, quiz.quiz.correctAns)
        currentQuiz.value = questionsList[currentQuestion]
    }

    fun triggerExamViolation(cheatFlagEntity: CheatFlagEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            cheatingStates.emit(CheatingStatus.valueOf(cheatFlagEntity.flagType))
            triggerExamViolationUseCase(cheatFlagEntity)
        }

    private fun addScoreToAttendedQuestion(isCorrect: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            val quiz = questionsList[currentQuestion]
            addCorrectAnsScoreUseCase(
                AddScoreToAttendedQuestionBodyParams(
                    examId,
                    quiz.quiz._id,
                    currentQuestion,
                    quiz.quiz.marks,
                    isCorrect,
                    (currentQuestion + 1) == questionsList.size,
                    courseId = courseId
                )
            ).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        quizStates.value = QuizState.NextQuestion
                        currentQuestion++
                        questionNumber.value = currentQuestion + 1
                        currentQuiz.value = questionsList[currentQuestion]
                    }
                    is NetworkResult.Loading -> {
                        quizStates.value = QuizState.SubmittingAnswer
                    }
                    is NetworkResult.Error -> {
                        quizStates.value = QuizState.Error(
                            it.message,
                            it.strResMessage ?: R.string.default_error_message
                        )
                    }
                }
            }
        }

    fun nextQuestion() {
        if (currentQuiz.value?.correctAns != null) {
            if (questionsList.size != (currentQuestion + 1)) {
                val quiz = questionsList[currentQuestion]
                addScoreToAttendedQuestion(quiz.userClick == quiz.quiz.correctAns)
            } else {
                quizStates.value = QuizState.Completed
            }
        } else {
            quizStates.value = QuizState.Error("", R.string.select_an_option_to_proceed)
        }
    }


    fun previousQuestion() {
        if ((currentQuestion - 1) >= 0) {
            currentQuestion--
            questionNumber.value = currentQuestion - 1
            currentQuiz.value = questionsList[currentQuestion]
        } else {
            quizStates.value = QuizState.Error(messageRes = R.string.this_is_the_first_question)
        }
    }

    fun updateUserActivity(body: UpdateActivityBodyParams) = viewModelScope.launch {
        updateUserActivityUseCase(body)
    }

}