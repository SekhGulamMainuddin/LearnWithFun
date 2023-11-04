package com.sekhgmainuddin.learnwithfun.presentation.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.FragmentQuizBinding


class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding: FragmentQuizBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz, container, false
        )
        return _binding!!.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displaymetrics = requireContext().resources.displayMetrics
        val screenHight = displaymetrics.heightPixels
        val screenWidth = displaymetrics.widthPixels

//        var lastAction = MotionEvent.ACTION_UP
        var dX = 0.0f
        var dY = 0.0f
        binding.dragMe.setOnTouchListener { dragView, event ->
            val newX: Float
            val newY: Float

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = dragView.x - event.rawX
                    dY = dragView.y - event.rawY
//                    lastAction = MotionEvent.ACTION_DOWN
                }

                MotionEvent.ACTION_MOVE -> {
                    newX = event.rawX + dX
                    newY = event.rawY + dY

                    // check if the view out of screen
                    if (newX <= 0 || newX >= screenWidth - dragView.width || newY <= 0 || newY >= screenHight - dragView.height) {
//                        lastAction = MotionEvent.ACTION_MOVE
                        return@setOnTouchListener true
                    }
                    dragView.x = newX
                    dragView.y = newY
//                    lastAction = MotionEvent.ACTION_MOVE
                }
//
//                MotionEvent.ACTION_UP -> if (lastAction == MotionEvent.ACTION_DOWN) Toast.makeText(
//                    requireContext(),
//                    "Clicked!",
//                    Toast.LENGTH_SHORT
//                ).show()

                else -> {
                    return@setOnTouchListener false
                }
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}