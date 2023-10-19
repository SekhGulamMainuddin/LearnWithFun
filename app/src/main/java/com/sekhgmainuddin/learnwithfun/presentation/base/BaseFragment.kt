package com.sekhgmainuddin.learnwithfun.presentation.base

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment(){
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

    protected fun showToast(message: String?, toastType: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, toastType).show()
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