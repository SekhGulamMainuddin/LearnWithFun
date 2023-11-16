package com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.AttendExamBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.SearchCoursesAndMentorBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.domain.modals.SearchItem
import com.sekhgmainuddin.learnwithfun.domain.use_case.enrollCourse.GetCourseDetailsUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.quiz.AttendQuizUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.searchCourse.SearchCourseAndMentorUseCase
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.uiStates.AttendQuizState
import com.sekhgmainuddin.learnwithfun.presentation.home.enrollCourse.uiStates.GetCourseDetailsState
import com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse.uiStates.CoursesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val getCourseDetailsUseCase: GetCourseDetailsUseCase,
    private val attendQuizUseCase: AttendQuizUseCase,
    private val searchCourseAndMentorUseCase: SearchCourseAndMentorUseCase
) : ViewModel() {

    private var _courseDetails =
        MutableStateFlow<GetCourseDetailsState>(GetCourseDetailsState.Initial)
    val courseDetails: StateFlow<GetCourseDetailsState>
        get() = _courseDetails

    val courseDetail = MutableStateFlow<CourseDetailDto?>(null)

    fun getCourseDetails(courseId: String) = viewModelScope.launch(Dispatchers.IO) {
        courseDetail.value = null
        getCourseDetailsUseCase(courseId).collect {
            when (it) {
                is NetworkResult.Success -> {
                    courseDetail.value = it.data
                    _courseDetails.value = GetCourseDetailsState.Success(it.data!!)
                }

                is NetworkResult.Loading -> {
                    _courseDetails.value = GetCourseDetailsState.Loading
                }

                is NetworkResult.Error -> {
                    _courseDetails.value = GetCourseDetailsState.Error(
                        it.message,
                        it.strResMessage ?: R.string.default_error_message
                    )
                }
            }
        }
    }

    private var _attendQuiz = MutableStateFlow<AttendQuizState>(AttendQuizState.Initial)
    val attendQuiz: StateFlow<AttendQuizState>
        get() = _attendQuiz

    fun attendQuiz(attendExamBodyParams: AttendExamBodyParams) =
        viewModelScope.launch(Dispatchers.IO) {
            attendQuizUseCase(attendExamBodyParams).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _attendQuiz.value = AttendQuizState.Success(it.data!!)
                    }

                    is NetworkResult.Loading -> {
                        _attendQuiz.value = AttendQuizState.Loading
                    }

                    is NetworkResult.Error -> {
                        _attendQuiz.value = AttendQuizState.Error(
                            it.message,
                            it.strResMessage ?: R.string.default_error_message
                        )
                    }
                }
            }
        }

    fun setFreshQuiz() {
        _attendQuiz.value = AttendQuizState.Initial
    }

    fun examIdAlreadyPresent(examId: String) {
        _attendQuiz.value = AttendQuizState.Success(examId)
    }

    private var _coursesSearchState = MutableStateFlow<CoursesState>(CoursesState.Initial)
    val coursesSearchState: StateFlow<CoursesState>
        get() = _coursesSearchState

    fun getCoursesAndMentors(searchCoursesAndMentorBodyParams: SearchCoursesAndMentorBodyParams) =
        viewModelScope.launch(Dispatchers.IO) {
            searchCourseAndMentorUseCase(
                searchCoursesAndMentorBodyParams
            ).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        val list = ArrayList<SearchItem>()
                        it.data!!.users.forEach { u ->
                            list.add(SearchItem(user = u))
                        }
                        it.data!!.courseList.forEach { c ->
                            list.add(SearchItem(course = c))
                        }
                        _coursesSearchState.value = CoursesState.Loaded(list)
                    }

                    is NetworkResult.Loading -> {
                        _coursesSearchState.value = CoursesState.Loading
                    }

                    is NetworkResult.Error -> {
                        _coursesSearchState.value = CoursesState.Error(
                            it.message,
                            it.strResMessage ?: R.string.default_error_message
                        )
                    }
                }
            }
        }

}