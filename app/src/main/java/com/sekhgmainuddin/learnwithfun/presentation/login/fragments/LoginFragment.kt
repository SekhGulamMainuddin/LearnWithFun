package com.sekhgmainuddin.learnwithfun.presentation.login.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.otpview.OTPListener
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.hideKeyboard
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.slideVisibility
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.databinding.FragmentLoginBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.HomeActivity
import com.sekhgmainuddin.learnwithfun.presentation.login.LoginSignUpViewModel
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.GetOTPState
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.VerifyOTPState
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    private val viewModel by activityViewModels<LoginSignUpViewModel>()
    private var countryCode: Int? = 91
    private var phoneNumber: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        initClickListeners()
        bindObservers()
    }

    private fun initClickListeners() {
        binding.apply {
            sendOtpButton.setOnClickListener {
                phoneNumber = phoneEditText.text.toString().replace(" ", "").toLong()
                sendOtpButton.text = getString(R.string.resend)
                this@LoginFragment.viewModel.getOTP(
                    GetOTPBodyParams(
                        countryCode = countryCode!!,
                        phoneNumber = phoneNumber!!
                    )
                )
            }
            phoneEditText.addTextChangedListener {
                if (it?.toString()?.replace(" ", "")?.length == 10) {
                    phoneNumberLayout.error = null
                    sendOtpButton.slideVisibility(true)
                } else {
                    if (sendOtpButton.isVisible) {
                        sendOtpButton.slideVisibility(false)
                        sendOtpButton.text = getString(R.string.send_otp)
                        otpView.setOTP("")
                    }
                }
            }
            continueButton.setOnClickListener {
                if (phoneEditText.text?.length != 10) {
                    phoneNumberLayout.error = getString(R.string.enter_phone_number)
                } else if (otpView.otp != null && otpView.otp!!.length == 6) {
                    this@LoginFragment.viewModel.verifyOTP(
                        VerifyOTPBodyParams(
                            countryCode = countryCode!!,
                            phoneNumber = phoneNumber!!,
                            otp = otpView.otp!!
                        )
                    )
                } else if (otpView.isVisible) {
                    otpView.showError()
                } else {
                    showSnackBar(R.string.verify_your_phone_number_first)
                }
            }
            otpView.otpListener = object : OTPListener {
                override fun onInteractionListener() {

                }

                override fun onOTPComplete(otp: String) {
                    hideKeyboard()
                }
            }
        }
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getOTPState.collect {
                        hideProgressBar()
                        when (it) {
                            is GetOTPState.Loading -> showProgressBar()
                            is GetOTPState.Sent -> {
                                binding.otpView.requestFocusOTP()
                                showToast(getString(R.string.otp_sent_successfully))
                            }

                            is GetOTPState.Error -> {
                                if (it.message.isEmpty()) {
                                    showToast(it.messageRes)
                                } else {
                                    showToast(it.message)
                                }
                            }
                        }
                    }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.verifyOTPState.collect {
                        hideProgressBar()
                        when (it) {
                            is VerifyOTPState.Loading -> showProgressBar()
                            is VerifyOTPState.NewUser -> {
                                findNavController().navigate(
                                    LoginFragmentDirections.actionLoginFragmentToSignUpFragment(
                                        countryCode!!,
                                        phoneNumber!!
                                    )
                                )
                            }

                            is VerifyOTPState.OldUser -> {
                                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                                requireActivity().finish()
                            }

                            VerifyOTPState.WrongOTP -> {
                                binding.otpView.showError()
                                binding.otpView.requestFocusOTP()
                                showSnackBar(R.string.wrong_otp_entered)
                            }

                            is VerifyOTPState.Error -> {
                                if (it.message.isEmpty()) {
                                    showToast(it.messageRes)
                                } else {
                                    showToast(it.message)
                                }
                            }

                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}