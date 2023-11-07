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
import com.sekhgmainuddin.learnwithfun.databinding.BottomSheetUploadDialogBinding
import com.sekhgmainuddin.learnwithfun.databinding.FragmentSignUpBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.HomeActivity
import com.sekhgmainuddin.learnwithfun.presentation.login.LoginSignUpViewModel
import com.sekhgmainuddin.learnwithfun.presentation.login.uiStates.CreateUserState
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
    private var verifiedEmail: String = ""

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
            lottieAnimationView.setOnClickListener {
                bottomSheetDialog.show()
            }
            profileImage.setOnClickListener {
                bottomSheetDialog.show()
            }
            createAccountButton.setOnClickListener {
                this@SignUpFragment.viewModel.createUser(
                    name = nameEditText.text.toString(),
                    email = verifiedEmail,
                    countryCode = args.countryCode,
                    phoneNumber = args.phoneNumber,
                    profilePicture = profilePic
                )
            }
            emailEditText.addTextChangedListener {
                emailInputLayout.error = ""
                if (verifiedEmail != it.toString()){
                    verifiedEmail = it.toString()
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
                                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}