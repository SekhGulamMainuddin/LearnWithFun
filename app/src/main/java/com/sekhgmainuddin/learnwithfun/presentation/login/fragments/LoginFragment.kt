package com.sekhgmainuddin.learnwithfun.presentation.login.fragments

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.slideVisibility
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.databinding.FragmentLoginBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.HomeActivity
import com.sekhgmainuddin.learnwithfun.presentation.login.GetOTPState
import com.sekhgmainuddin.learnwithfun.presentation.login.LoginSignUpViewModel
import com.sekhgmainuddin.learnwithfun.presentation.login.VerifyOTPState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    private val viewModel by activityViewModels<LoginSignUpViewModel>()
    private var countryCode: Int? = null
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

        initClickListeners()
        bindObservers()

    }

    private fun initClickListeners() {
        binding.apply {
            ccp.registerCarrierNumberEditText(phoneEditText)
            ccp.setPhoneNumberValidityChangeListener {
                if (it) {
                    sendOtpButton.slideVisibility(true)
                } else {
                    sendOtpButton.isVisible = false
                }
            }
            sendOtpButton.setOnClickListener {
                sendOtpButton.text = getString(R.string.resend)
                if (phoneEditText.text.toString()
                        .isNotEmpty()
                ) {
                    phoneEditText.isEnabled = false
                    otpTitle.visibility = View.VISIBLE
                    otpDescription.visibility = View.VISIBLE
                    otpView.visibility = View.VISIBLE
                    countryCode = ccp.selectedCountryCodeAsInt
                    phoneNumber = phoneEditText.text.toString().replace(" ", "").toLong()
                    viewModel.getOTP(
                        GetOTPBodyParams(
                            countryCode = countryCode!!,
                            phoneNumber = phoneNumber!!
                        )
                    )
                } else if (binding.phoneEditText.text.toString().isEmpty())
                    showToast("Enter valid Number")
                else {
                    otpView.error = "Enter the OTP"
                    showToast(otpView.text.toString())
                }
            }
            continueButton.setOnClickListener {
                viewModel.verifyOTP(
                    VerifyOTPBodyParams(
                        countryCode = countryCode!!,
                        phoneNumber = phoneNumber!!,
                        otp = otpView.text.toString()
                    )
                )
            }
        }
    }

    private val otpTimer = object : CountDownTimer(120000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.otpTimeLeft.text =
                if (millisUntilFinished > 60000) "0" + SimpleDateFormat("M:ss").format(
                    Date(millisUntilFinished)
                ) else "00" + SimpleDateFormat(":ss").format(Date(millisUntilFinished))
        }

        override fun onFinish() {
            binding.otpTimeLeft.text = "00:00"
            binding.phoneNumberCV.isEnabled = true
        }
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getOTPState.collect {
                    hideProgressBar()
                    when (it) {
                        is GetOTPState.Initial -> {}
                        is GetOTPState.Loading -> showProgressBar()
                        is GetOTPState.Sent -> {
                            binding.otpTimeLeft.isVisible = true
                            otpTimer.start()
                            showToast(getString(R.string.otp_sent_successfully))
                        }
                        is GetOTPState.Error -> showToast(it.error)
                    }
                }
            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.verifyOTPState.collect {
                    hideProgressBar()
                    when (it) {
                        is VerifyOTPState.Initial -> {}
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
                        is VerifyOTPState.Error -> {
                            showToast(it.error)
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