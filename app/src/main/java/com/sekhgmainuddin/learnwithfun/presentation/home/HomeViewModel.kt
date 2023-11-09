package com.sekhgmainuddin.learnwithfun.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.data.dto.UserDto
import com.sekhgmainuddin.learnwithfun.domain.use_case.home.GetUserDetailsUseCase
import com.sekhgmainuddin.learnwithfun.presentation.home.home.uiStates.GetUserDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) : ViewModel() {

    private var _userDetails = MutableStateFlow<GetUserDetailsState>(GetUserDetailsState.Initial)
    val userDetails: StateFlow<GetUserDetailsState>
        get() = _userDetails

    val user = MutableStateFlow<UserDto?>(null)

    fun getUserDetails() = viewModelScope.launch {
        getUserDetailsUseCase().collect {
            when (it) {
                is NetworkResult.Success -> {
                    user.value = it.data!!.user
                    _userDetails.value = GetUserDetailsState.Success(it.data!!)
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


}