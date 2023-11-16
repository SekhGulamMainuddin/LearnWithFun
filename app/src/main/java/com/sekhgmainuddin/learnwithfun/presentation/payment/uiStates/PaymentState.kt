package com.sekhgmainuddin.learnwithfun.presentation.payment.uiStates

import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.dto.PaymentTokenDto

sealed class PaymentState {
    data object Initial : PaymentState()
    data object FetchingToken : PaymentState()
    data class PaymentTokenFetched(val paymentToken: PaymentTokenDto) : PaymentState()
    data object DoingPayment : PaymentState()
    data object VerifyingPayment : PaymentState()
    data object PaymentVerified : PaymentState()
    data class Error(val message: String = "", val messageRes: Int = R.string.default_error_message) : PaymentState()
}
