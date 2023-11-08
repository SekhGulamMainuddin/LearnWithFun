package com.sekhgmainuddin.learnwithfun.presentation.login.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.getBitmap
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.hideKeyboard
import com.sekhgmainuddin.learnwithfun.databinding.BottomSheetUploadDialogBinding
import com.sekhgmainuddin.learnwithfun.databinding.FragmentSignUpBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.HomeActivity
import com.sekhgmainuddin.learnwithfun.presentation.login.LoginSignUpViewModel
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.CreateUserState
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.SendMailState
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.VerifyMailState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding
        get() = _binding!!
    private val args: SignUpFragmentArgs by navArgs()

    private val viewModel by activityViewModels<LoginSignUpViewModel>()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var profilePic: Bitmap? = null
    private var requestedEmail: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_up, container, false
        )
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
        setupBottomSheetImageUploadDialog()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        registerClickListeners()
    }

    private fun registerClickListeners() {
        binding.apply {
            toggleOtpViewsVisibility(false)
            lottieAnimationView.setOnClickListener {
                bottomSheetDialog.show()
            }
            profileImage.setOnClickListener {
                bottomSheetDialog.show()
            }
            sendAndVerifyEmail.setOnClickListener {
                if (otpView.isVisible) {
                    this@SignUpFragment.viewModel.verifyMail(requestedEmail, otpView.otp)
                } else {
                    this@SignUpFragment.viewModel.sendMail(emailEditText.text.toString())
                }
            }
            createAccountButton.setOnClickListener {
                this@SignUpFragment.viewModel.createUser(
                    name = nameEditText.text.toString(),
                    email = requestedEmail,
                    countryCode = args.countryCode,
                    phoneNumber = args.phoneNumber,
                    profilePicture = profilePic
                )
            }
            emailEditText.addTextChangedListener {
                if (emailInputLayout.error?.isNotEmpty() == true) {
                    emailInputLayout.error = ""
                }
                if (otpView.isVisible) {
                    toggleOtpViewsVisibility(false)
                }
            }
        }
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.userType.collect {
                        it?.let {
                            binding.parentLayout.fullScroll(ScrollView.FOCUS_DOWN)
                        }
                    }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.createUserState.collect {
                        hideProgressBar()
                        binding.apply {
                            when (it) {
                                CreateUserState.Initial -> {}
                                CreateUserState.EmailNotVerified -> {
                                    emailInputLayout.error = getString(R.string.email_not_verified)
                                    showSnackBar(R.string.email_not_verified)
                                }

                                CreateUserState.Loading -> {
                                    showProgressBar()
                                }

                                CreateUserState.NameNotAdded -> {
                                    nameInputLayout.error = getString(R.string.enter_your_full_name)
                                    showSnackBar(R.string.enter_your_full_name)
                                }

                                CreateUserState.ProfilePicUploadFailed -> {
                                    showSnackBar(R.string.profile_pic_upload_failed)
                                }

                                CreateUserState.UserCreated -> {
                                    showToast(R.string.account_created_successfully)
                                    startActivity(
                                        Intent(
                                            requireActivity(),
                                            HomeActivity::class.java
                                        )
                                    )
                                    requireActivity().finish()
                                }

                                CreateUserState.UserTypeNotSpecified -> {
                                    parentLayout.fullScroll(ScrollView.FOCUS_DOWN)
                                    showSnackBar(R.string.specify_user_type)
                                }

                                is CreateUserState.Error -> {
                                    showSnackBar(it.message)
                                }
                            }
                        }
                    }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.sendMailState.collect {
                        hideProgressBar()
                        binding.apply {
                            when (it) {
                                is SendMailState.EmailAlreadyTaken -> {
                                    emailInputLayout.error =
                                        getString(R.string.email_is_already_taken)
                                }

                                is SendMailState.Loading -> showProgressBar()
                                is SendMailState.Success -> {
                                    otpView.requestFocusOTP()
                                    requestedEmail = emailEditText.text.toString()
                                    showSnackBar(
                                        resources.getString(
                                            R.string.email_sent_successfully,
                                            requestedEmail
                                        )
                                    )
                                    toggleOtpViewsVisibility(true)
                                }

                                is SendMailState.InvalidEmail -> {
                                    emailInputLayout.error = getString(R.string.enter_a_valid_email)
                                    showSnackBar(R.string.enter_a_valid_email)
                                }

                                is SendMailState.Error -> {
                                    if (it.message.isNotEmpty()) {
                                        showSnackBar(it.message)
                                    } else {
                                        showSnackBar(it.messageRes)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.verifyMailState.collect {
                        hideProgressBar()
                        binding.apply {
                            when (it) {
                                VerifyMailState.Success -> {
                                    showSnackBar(R.string.email_verfied_successfully)
                                }

                                VerifyMailState.EmailNotFound -> {
                                    emailInputLayout.error = getString(R.string.email_not_found)
                                    showSnackBar(R.string.email_not_found)
                                }

                                VerifyMailState.VerificationCodeNotEntered -> {
                                    otpView.showError()
                                    otpView.requestFocusOTP()
                                    showSnackBar(R.string.please_enter_the_verification_code)
                                }

                                VerifyMailState.WrongVerificationCode -> {
                                    otpView.showError()
                                    showSnackBar(R.string.verification_code_not_valid)
                                }

                                VerifyMailState.Loading -> showProgressBar()
                                is VerifyMailState.Error -> {
                                    if (it.message.isNotEmpty()) {
                                        showSnackBar(it.message)
                                    } else {
                                        showSnackBar(it.messageRes)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupBottomSheetImageUploadDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding: BottomSheetUploadDialogBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.bottom_sheet_upload_dialog,
            null,
            false
        )
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.apply {
            camera.setOnClickListener {
                cameraLaunch.launch()
                bottomSheetDialog.dismiss()
            }
            cameraTV.setOnClickListener {
                cameraLaunch.launch()
                bottomSheetDialog.dismiss()
            }
            gallery.setOnClickListener {
                galleryLaunch.launch("image/*")
                bottomSheetDialog.dismiss()
            }
            galleryTV.setOnClickListener {
                galleryLaunch.launch("image/*")
                bottomSheetDialog.dismiss()
            }
        }
    }

    private val galleryLaunch = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            profilePic = it.getBitmap(requireContext().contentResolver)
        }
        changeProfileImageVisibility(it != null)
    }

    private val cameraLaunch =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            it?.let {
                profilePic = it
            }
            changeProfileImageVisibility(it != null)
        }

    private fun changeProfileImageVisibility(showProfilePic: Boolean) {
        binding.apply {
            lottieAnimationView.visibility = if (showProfilePic) View.INVISIBLE else View.VISIBLE
            profileImage.visibility = if (showProfilePic) View.VISIBLE else View.INVISIBLE
            profilePic?.let {
                profileImage.setImageBitmap(it)
            }
        }
    }

    private fun toggleOtpViewsVisibility(showOtpViews: Boolean) {
        binding.apply {
            sendAndVerifyEmail.text =
                getString(if (showOtpViews) R.string.verify_email else R.string.send_email)
            otpView.isVisible = showOtpViews
            otpDescription.isVisible = showOtpViews
            otpTitle.isVisible = showOtpViews
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}