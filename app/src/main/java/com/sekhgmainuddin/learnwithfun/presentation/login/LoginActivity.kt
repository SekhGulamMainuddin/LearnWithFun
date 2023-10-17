package com.sekhgmainuddin.learnwithfun.presentation.login

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.sekhgmainuddin.learnwithfun.databinding.ActivityLoginBinding
import com.sekhgmainuddin.learnwithfun.common.NetworkResult
import com.sekhgmainuddin.learnwithfun.common.utils.Utils.slideVisibility
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: Dialog
    private lateinit var mVerificationId: String
    private val viewModel by viewModels<LoginSignUpViewModel>()
    private lateinit var snackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

//        initClickListeners()
//        bindObservers()

    }

//    private fun initClickListeners() {
//        binding.apply{
//            ccp.registerCarrierNumberEditText(phoneEditText)
//            ccp.setPhoneNumberValidityChangeListener {
//                if (it) {
//                    sendOtpButton.slideVisibility(true)
//                } else {
//                    sendOtpButton.isVisible = false
//                }
//            }
//            sendOtpButton.setOnClickListener {
//                if (mResendToken==null){
//                    binding.sendOtpButton.text= getString(R.string.resend)
//                    if(binding.phoneEditText.text.toString().isNotEmpty() && binding.phoneEditText.isEnabled && !binding.otpView.isVisible){
//                        binding.phoneEditText.isEnabled= false
//                        binding.otpTitle.visibility= View.VISIBLE
//                        binding.otpDescription.visibility= View.VISIBLE
//                        binding.otpView.visibility= View.VISIBLE
//                        progressDialog.show()
//                        startPhoneNumberVerification(ccp.fullNumberWithPlus)
//                    }
//                    else if(binding.phoneEditText.text.toString().isEmpty())
//                        Toast.makeText(this@LoginActivity, "Enter valid Number", Toast.LENGTH_SHORT).show()
//                    else{
//                        binding.otpView.error= "Enter the OTP"
//                        Toast.makeText(this@LoginActivity, binding.otpView.text.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    progressDialog.show()
//                    resendVerificationCode(ccp.fullNumberWithPlus, mResendToken!!)
//                }
//            }
//            continueButton.setOnClickListener {
//                progressDialog.show()
//                verifyPhoneNumberWithCode(mVerificationId, otpView.text.toString())
//            }
//        }
//    }
//
//    private fun bindObservers(){
//        viewModel.phoneLoginSignUp.observe(this){
//            progressDialog.dismiss()
//            when(it){
//                is NetworkResult.Success -> {
//                    otpTimer.cancel()
//                    if (it.statusCode==200){
//                        showSnackBar("Logged in successfully")
////                        startActivity(Intent(this, MainActivity::class.java))
//                        finish()
//                    }
//                    else if (it.statusCode==201){
//                        showSnackBar("Sign-Up Successful")
//
//                    }
//
//                }
//                is NetworkResult.Error -> {
//
//                }
//                is NetworkResult.Loading -> {
//                    progressDialog.show()
//                }
//            }
//        }
//    }
//
//    private fun showSnackBar(message: String) {
//        snackBar = Snackbar.make(binding.parentLayout, message, Snackbar.LENGTH_SHORT)
//        snackBar.show()
//    }
//
//    private val otpTimer = object : CountDownTimer(120000, 1000) {
//        override fun onTick(millisUntilFinished: Long) {
//            binding.otpTimeLeft.text = if(millisUntilFinished>60000) "0"+ SimpleDateFormat("M:ss").format(
//                Date(millisUntilFinished)
//            ) else "00"+ SimpleDateFormat(":ss").format(Date(millisUntilFinished))
//        }
//        override fun onFinish() {
//            binding.otpTimeLeft.text = "00:00"
//            binding.phoneNumberCV.isEnabled= true
//        }
//    }
//
//    private val mCallbacks= object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            viewModel.phoneLoginSignUp(credential)
//            progressDialog.dismiss()
//        }
//
//        override fun onVerificationFailed(e: FirebaseException) {
//            progressDialog.dismiss()
//            if (e is FirebaseAuthInvalidCredentialsException) {
//                //Toast.makeText(this, "Verification Failed due to Invalid Number", Toast.LENGTH_SHORT).show()
//            } else if (e is FirebaseTooManyRequestsException) {
//                Toast.makeText(this@LoginActivity, "Too Many Requests Generated. Please try after some time.", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        override fun onCodeSent(
//            verificationId: String,
//            token: PhoneAuthProvider.ForceResendingToken
//        ) {
//            binding.otpTimeLeft.visibility= View.VISIBLE
//            otpTimer.start()
//            mVerificationId = verificationId
//            mResendToken = token
//            progressDialog.dismiss()
//        }
//    }
//
//    private fun startPhoneNumberVerification(phoneNumber: String) {
//        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(120L, TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(mCallbacks)
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    private fun resendVerificationCode(
//        phoneNumber: String,
//        token: PhoneAuthProvider.ForceResendingToken
//    ) {
//        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(120L, TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(mCallbacks)
//            .setForceResendingToken(token)
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
//        val credential = PhoneAuthProvider.getCredential(verificationId, code)
//        viewModel.phoneLoginSignUp(credential)
//    }


}