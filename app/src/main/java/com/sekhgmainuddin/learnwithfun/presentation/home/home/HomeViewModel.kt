package com.sekhgmainuddin.learnwithfun.presentation.home.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.data.db.entities.CheatFlagEntity
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.UpdateActivityBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.UserDetailDto
import com.sekhgmainuddin.learnwithfun.domain.modals.HomeViewContent
import com.sekhgmainuddin.learnwithfun.domain.use_case.activity.RetryUpdateActivityUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.activity.UpdateUserActivityUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.home.GetAllUploadFailedCheatAlerts
import com.sekhgmainuddin.learnwithfun.domain.use_case.home.GetPopularCoursesUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.home.GetUserDetailsUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.quiz.TriggerExamViolationUseCase
import com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates.GetPopularCoursesState
import com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates.GetUserDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val getPopularCoursesUseCase: GetPopularCoursesUseCase,
    private val getAllUploadFailedCheatAlerts: GetAllUploadFailedCheatAlerts,
    private val triggerExamViolationUseCase: TriggerExamViolationUseCase,
    private val updateUserActivityUseCase: UpdateUserActivityUseCase,
    private val retryUpdateActivityUseCase: RetryUpdateActivityUseCase
) : ViewModel() {

    private var _userDetails = MutableStateFlow<GetUserDetailsState>(GetUserDetailsState.Initial)
    val userDetails: StateFlow<GetUserDetailsState>
        get() = _userDetails

    val userDetail = MutableStateFlow<UserDetailDto?>(null)

    private var popularCourseList = HomeViewContent(emptyList())
    private var homeViewContent = mutableListOf<HomeViewContent>()

    fun getUserDetails() = viewModelScope.launch(Dispatchers.IO) {
        getUserDetailsUseCase().collect {
            when (it) {
                is NetworkResult.Success -> {
                    userDetail.value = it.data!!
                    val contentList = it.data!!.enrolled_courses.map { course ->
                        HomeViewContent(
                            enrolledCourseProgress = course
                        )
                    }.toList()
                    homeViewContent.clear()
                    homeViewContent.add(popularCourseList)
                    homeViewContent.add(HomeViewContent())
                    homeViewContent.addAll(contentList)
                    // Done because ListAdapter DiffUtil doesn't call on same list
                    _userDetails.value = GetUserDetailsState.Success(homeViewContent.toList())
                }

                is NetworkResult.Loading -> {
                    _userDetails.value = GetUserDetailsState.Loading
                }

                is NetworkResult.Error -> {
                    _userDetails.value = GetUserDetailsState.Error(
                        it.message,
                        it.strResMessage ?: R.string.default_error_message
                    )
                }
            }
        }
    }

    private var _popularCourses =
        MutableStateFlow<GetPopularCoursesState>(GetPopularCoursesState.Initial)
    val popularCourses: StateFlow<GetPopularCoursesState>
        get() = _popularCourses

    fun getPopularCourses() = viewModelScope.launch(Dispatchers.IO) {
        getPopularCoursesUseCase().collect {
            when (it) {
                is NetworkResult.Success -> {
                    popularCourseList = it.data!!
                    if (homeViewContent.isNotEmpty()) {
                        homeViewContent[0] = popularCourseList
                        _popularCourses.value =
                            GetPopularCoursesState.Success(homeViewContent.toList())
                    }
                    // Done because ListAdapter DiffUtil doesn't call on same list
                }

                is NetworkResult.Loading -> {
                    _popularCourses.value = GetPopularCoursesState.Loading
                }

                is NetworkResult.Error -> {
                    _popularCourses.value = GetPopularCoursesState.Error(
                        it.message,
                        it.strResMessage ?: R.string.default_error_message
                    )
                }
            }
        }
    }

    fun retryUploadingCheatFlags() = viewModelScope.launch(Dispatchers.IO) {
        try {
            getAllUploadFailedCheatAlerts(Unit)
            getAllUploadFailedCheatAlerts().catch {
                Log.d("retryUploadingCheatFlags", "retryUploadingCheatFlags: ${it.message}")
            }.collect {
                if(it.isEmpty()){
                    yield()
                    awaitCancellation()
                }
                it.forEach { c ->
                    if (!retryMap.containsKey(c.dateTime) && c.retryCount < 5) {
                        Log.d("retryUploadingCheatFlags", "retryUploadingCheatFlags: ${it}")
                        retryMap[c.dateTime] = c
                        retryUploadCheatQueue.push(c)
                    }
                }
                if (!isRetryRunning && retryUploadCheatQueue.isNotEmpty()) {
                    uploadCheatingFlags()
                }
            }
        }catch (e: Exception) {
            Log.d("RetryException", "retryUploadingCheatFlags: $e")
        }
    }

    private val retryMap = HashMap<Long, CheatFlagEntity>()
    private val retryUploadCheatQueue = LinkedList<CheatFlagEntity>()
    private var isRetryRunning = false
    private fun uploadCheatingFlags() = viewModelScope.launch(Dispatchers.IO) {
        isRetryRunning = true
        while (retryUploadCheatQueue.isNotEmpty()) {
            val it = retryUploadCheatQueue.first
            triggerExamViolationUseCase(it)
            retryMap.remove(it.dateTime)
            retryUploadCheatQueue.pop()
        }
        isRetryRunning = false
    }

    fun updateUserActivity(body: UpdateActivityBodyParams) = viewModelScope.launch(Dispatchers.IO) {
        updateUserActivityUseCase(body)
    }

    fun retryUpdateActivity() = viewModelScope.launch(Dispatchers.IO) {
        retryUpdateActivityUseCase()
    }

}