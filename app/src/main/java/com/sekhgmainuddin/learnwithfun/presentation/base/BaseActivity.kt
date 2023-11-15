package com.sekhgmainuddin.learnwithfun.presentation.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity(), BaseActivityCallback {

    @Named("progressDialog")
    @Inject
    lateinit var progressDialog: Dialog

    protected fun showToast(message: String, toastType: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this@BaseActivity, message, toastType).show()
    }

    protected fun showToast(message: Int, toastType: Int = Toast.LENGTH_SHORT) {
        showToast(getString(message), toastType)
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    protected fun showSnackBar(message: String, snackBarDuration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message, snackBarDuration,
        ).show()
    }

    protected fun showSnackBar(message: Int, snackBarDuration: Int = Snackbar.LENGTH_SHORT) {
        showSnackBar(getString(message), snackBarDuration)
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