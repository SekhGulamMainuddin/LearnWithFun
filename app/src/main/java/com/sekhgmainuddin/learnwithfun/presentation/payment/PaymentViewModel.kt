package com.sekhgmainuddin.learnwithfun.presentation.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.helper.NetworkResult
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyPaymentBodyParams
import com.sekhgmainuddin.learnwithfun.domain.use_case.payment.GetPaymentTokenUseCase
import com.sekhgmainuddin.learnwithfun.domain.use_case.payment.VerifyPaymentUseCase
import com.sekhgmainuddin.learnwithfun.presentation.payment.uiStates.PaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getPaymentTokenUseCase: GetPaymentTokenUseCase,
    private val verifyPaymentUseCase: VerifyPaymentUseCase
) : ViewModel() {

    private var _paymentState = MutableStateFlow<PaymentState>(PaymentState.Initial)
    val paymentState: StateFlow<PaymentState>
        get() = _paymentState

    fun getPaymentToken() = viewModelScope.launch(Dispatchers.IO) {
        getPaymentTokenUseCase().collect {
            when (it) {
                is NetworkResult.Success -> {
                    _paymentState.value = PaymentState.PaymentTokenFetched(it.data!!)
                }

                is NetworkResult.Loading -> {
                    _paymentState.value = PaymentState.FetchingToken
                }

                is NetworkResult.Error -> {
                    _paymentState.value = PaymentState.Error(
                        it.message,
                        it.strResMessage ?: R.string.failed_to_fetch_payment_token
                    )
                }
            }
        }
    }

    fun setPaymentStateDoingPayment() = viewModelScope.launch {
        _paymentState.value = PaymentState.DoingPayment
    }

    fun setPaymentStateCancelledOrError(message: String)  = viewModelScope.launch{
        _paymentState.value = PaymentState.Error(message = message)
    }

    fun verifyPayment(verifyPaymentBodyParams: VerifyPaymentBodyParams) =
        viewModelScope.launch(Dispatchers.IO) {
            verifyPaymentUseCase(verifyPaymentBodyParams).collect {
                when(it) {
                    is NetworkResult.Success -> {
                        _paymentState.value = PaymentState.PaymentVerified
                    }
                    is NetworkResult.Loading -> {
                        _paymentState.value = PaymentState.VerifyingPayment
                    }
                    is NetworkResult.Error -> {
                        _paymentState.value = PaymentState.Error(
                            it.message,
                            it.strResMessage ?: R.string.failed_to_fetch_payment_token
                        )
                    }
                }
            }
        }

}