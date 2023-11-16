package com.sekhgmainuddin.learnwithfun.presentation.home.enrollCourse

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.FragmentStartPaymentBinding
import com.sekhgmainuddin.learnwithfun.presentation.payment.PaymentActivity
import com.sekhgmainuddin.learnwithfun.presentation.payment.PaymentViewModel
import com.sekhgmainuddin.learnwithfun.presentation.payment.uiStates.PaymentState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class StartPaymentFragment : DialogFragment() {

    private var _binding: FragmentStartPaymentBinding? = null
    private val binding: FragmentStartPaymentBinding
        get() = _binding!!

    private val viewModel by viewModels<PaymentViewModel>()
    private val args: StartPaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_start_payment, container, false
        )
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.getPaymentToken()

        bindObservers()
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paymentState.collect {
                    when (it) {
                        PaymentState.Initial -> {}
                        PaymentState.FetchingToken -> {}
                        is PaymentState.PaymentTokenFetched -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.starting_payment),
                                Toast.LENGTH_SHORT
                            ).show()
                            delay(500)
                            val paymentIntent = Intent(requireContext(), PaymentActivity::class.java)
                            paymentIntent.putExtra("paymentAmount", args.paymentAmount)
                            paymentIntent.putExtra("courseId", args.courseId)
                            paymentIntent.putExtra("paymentToken", it.paymentToken.paymentToken)
                            paymentResult.launch(paymentIntent)
                            dismiss()
                        }

                        PaymentState.DoingPayment -> {}
                        PaymentState.PaymentVerified -> {}

                        PaymentState.VerifyingPayment -> {}
                        is PaymentState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                it.message.ifEmpty { getString(it.messageRes) },
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        }
                    }
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val parentFragment: Fragment? = parentFragment
        if (parentFragment is DialogInterface.OnDismissListener) {
            (parentFragment as DialogInterface.OnDismissListener?)!!.onDismiss(dialog)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val paymentResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == -101) {
            Toast.makeText(
                requireContext(),
                getString(R.string.default_error_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}