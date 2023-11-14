package com.sekhgmainuddin.learnwithfun.presentation.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
open class BaseFragment : Fragment() {
    private var callback: BaseActivityCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = if (context is Activity) context else null
        callback = try {
            activity as BaseActivityCallback?
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString() +
                        " must implement BaseActivityCallback methods",
            )
        }
    }

    protected fun showToast(message: String, toastDuration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, toastDuration).show()
    }

    protected fun showToast(message: Int, toastDuration: Int = Toast.LENGTH_SHORT) {
        showToast(getString(message), toastDuration)
    }

    protected fun showSnackBar(message: String, snackBarDuration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message, snackBarDuration,
        ).show()
    }

    protected fun showSnackBar(message: Int, snackBarDuration: Int = Snackbar.LENGTH_SHORT) {
        showSnackBar(getString(message), snackBarDuration)
    }

    protected fun showProgressBar() {
        callback?.showProgressDialog()
    }

    protected fun hideProgressBar() {
        callback?.hideProgressDialog()
    }

    protected fun showLoadingBar() {
        callback?.showLoadingDialog()
    }

    protected fun hideLoadingBar() {
        callback?.hideLoadingDialog()
    }

    protected fun showKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, 0)
    }

    protected fun pressBack() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}