package com.sekhgmainuddin.learnwithfun.presentation.quiz

import androidx.lifecycle.ViewModel
import com.airbnb.lottie.animation.content.Content
import com.sekhgmainuddin.learnwithfun.common.enums.CheatingStatus
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.ContentDto
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.QuizDto
import com.sekhgmainuddin.learnwithfun.domain.modals.Quiz
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor() : ViewModel() {

    val currentQuiz = MutableStateFlow<Quiz?>(null)
    val questionNumber = MutableStateFlow<Int>(1)
    val questionsList = ArrayList<QuizDto>()
    private var currentQuestion = 0

    fun setQuestions(contentDto: ContentDto) {
        questionsList.clear()
        contentDto.quiz?.forEach {
            questionsList.add(it)
        }
        currentQuestion = 0
        val quiz = Quiz(questionsList[currentQuestion])
        currentQuiz.value = quiz
    }

    fun checkAnswer(option: Int) {
        currentQuiz.value =
            Quiz(questionsList[currentQuestion], option, questionsList[currentQuestion].correctAns)
    }

    fun triggerExamViolation(cheatingStatus: CheatingStatus) {

    }

    fun nextQuestion() {
        if (questionsList.size != (currentQuestion + 1)) {
            currentQuestion++
            questionNumber.value = currentQuestion+1
            currentQuiz.value = Quiz(questionsList[currentQuestion])
        } else {

        }
    }

}