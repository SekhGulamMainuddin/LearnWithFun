package com.sekhgmainuddin.learnwithfun.presentation.payment

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.braintreepayments.api.DropInClient
import com.braintreepayments.api.DropInListener
import com.braintreepayments.api.DropInRequest
import com.braintreepayments.api.DropInResult
import com.braintreepayments.api.GooglePayRequest
import com.braintreepayments.api.PayPalVaultRequest
import com.google.android.gms.wallet.TransactionInfo
import com.google.android.gms.wallet.WalletConstants
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyPaymentBodyParams
import com.sekhgmainuddin.learnwithfun.databinding.ActivityPaymentBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity
import com.sekhgmainuddin.learnwithfun.presentation.payment.uiStates.PaymentState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaymentActivity : BaseActivity(), DropInListener {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var dropInClient: DropInClient
    private var paymentToken: String? = null
    private var paymentAmount: String? = null
    private var courseId: String? = null
    private var deviceData: String? = null

    private val viewModel by viewModels<PaymentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        paymentAmount = intent.getStringExtra("paymentAmount")
        courseId = intent.getStringExtra("courseId")
        paymentToken = intent.getStringExtra("paymentToken")

        if (courseId == null || paymentAmount == null) {
            setResult(-101)
            finish()
        } else {
            launchPayment()
        }
        bindObservers()
    }

    override fun onStart() {
        super.onStart()
        launchDropIn()
    }
    private fun bindObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paymentState.collect {
                    when (it) {
                        PaymentState.Initial -> {}
                        PaymentState.FetchingToken -> {}
                        is PaymentState.PaymentTokenFetched -> {
                            paymentToken = it.paymentToken.paymentToken
                            showSnackBar(R.string.starting_payment)
                        }

                        PaymentState.DoingPayment -> {}
                        PaymentState.PaymentVerified -> {
                            showSnackBar(R.string.course_enrolled_successfully)
                            delay(1000)
                            setResult(1)
                            finish()
                        }

                        PaymentState.VerifyingPayment -> {}
                        is PaymentState.Error -> {
                            if (it.message.isEmpty()) {
                                showSnackBar(it.messageRes)
                            } else {
                                showSnackBar(it.message)
                            }
                            delay(1000)
                            setResult(-111)
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun launchPayment() {
        dropInClient = DropInClient(this, paymentToken)
        dropInClient.setListener(this)
    }

    private fun launchDropIn() {
        val dropInRequest = DropInRequest()
        val googlePayRequest = GooglePayRequest()
        googlePayRequest.transactionInfo = TransactionInfo.newBuilder()
            .setTotalPrice(paymentAmount!!)
            .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
            .setCurrencyCode("INR")
            .build()
        googlePayRequest.isBillingAddressRequired = true
        dropInRequest.googlePayRequest = googlePayRequest

        val payPalVaultRequest = PayPalVaultRequest()
        dropInRequest.payPalRequest = payPalVaultRequest
        dropInClient.launchDropIn(dropInRequest)
        viewModel.setPaymentStateDoingPayment()
    }

    override fun onDropInSuccess(dropInResult: DropInResult) {
        deviceData = dropInResult.deviceData
        showSnackBar(R.string.payment_done)
        viewModel.verifyPayment(
            VerifyPaymentBodyParams(
                nonce = dropInResult.paymentMethodNonce!!.string,
                deviceData = deviceData!!,
                paymentAmount!!,
                courseId!!
            )
        )
    }

    override fun onDropInFailure(error: Exception) {
        Log.d("whatistheerror", "onDropInFailure: $error")
        viewModel.setPaymentStateCancelledOrError(getString(R.string.payment_failed))
    }
}