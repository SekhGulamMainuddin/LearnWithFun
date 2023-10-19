package com.sekhgmainuddin.learnwithfun.presentation.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity(), BaseActivityCallback {

    @Inject
    lateinit var progressDialog: Dialog

    protected fun showToast(message: String?, toastType: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this@BaseActivity, message, toastType).show()
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    companion object {
        fun hideKeyboard(context: Context) {
            val activity = context as Activity
            val inputMethodManager = context.getSystemService(
                Activity.INPUT_METHOD_SERVICE,
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                0,
            )
        }
    }

}