package com.sekhgmainuddin.learnwithfun.presentation.login.fragments

import android.content.Intent
import android.os.Build
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
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.hideKeyboard
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.slideVisibility
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.GetOTPBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.body_params.VerifyOTPBodyParams
import com.sekhgmainuddin.learnwithfun.databinding.FragmentLoginBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.HomeActivity
import com.sekhgmainuddin.learnwithfun.presentation.login.GetOTPState
import com.sekhgmainuddin.learnwithfun.presentation.login.LoginSignUpViewModel
import com.sekhgmainuddin.learnwithfun.presentation.login.VerifyOTPState
import kotlinx.coroutines.launch

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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
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
                    ccp.isEnabled = false
                    phoneEditText.isEnabled = false
                    countryCode = ccp.selectedCountryCodeAsInt
                    phoneNumber = ccp.fullNumber.substring(ccp.selectedCountryCode.length).toLong()
                    this@LoginFragment.viewModel.getOTP(
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
                this@LoginFragment.viewModel.verifyOTP(
                    VerifyOTPBodyParams(
                        countryCode = countryCode!!,
                        phoneNumber = phoneNumber!!,
                        otp = otpView.text.toString()
                    )
                )
            }
            otpView.addTextChangedListener {
                if (it?.length == 6) {
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
                            is GetOTPState.Initial -> {}
                            is GetOTPState.Loading -> showProgressBar()
                            is GetOTPState.Sent -> {
                                binding.apply {
                                    otpTimeLeft.isVisible = true
                                    otpTitle.visibility = View.VISIBLE
                                    otpDescription.visibility = View.VISIBLE
                                    otpView.visibility = View.VISIBLE
                                }
                                showToast(getString(R.string.otp_sent_successfully))
                            }

                            is GetOTPState.Error -> showToast(it.error)
                        }
                    }
                }
            }
            launch {
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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}