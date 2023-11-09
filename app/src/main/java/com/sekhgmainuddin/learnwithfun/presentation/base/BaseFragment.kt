package com.sekhgmainuddin.learnwithfun.presentation.base

import android.app.Activity
import android.content.Context
import android.widget.Toast
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

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}