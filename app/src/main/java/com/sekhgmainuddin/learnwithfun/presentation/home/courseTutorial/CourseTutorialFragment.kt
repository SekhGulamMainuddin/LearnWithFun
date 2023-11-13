package com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.databinding.FragmentCourseTutorialBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.courseVideo.CourseVideoActivity
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.CourseContentAdapter
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.OnCourseContentClickListener
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.WeeksAdapter
import com.sekhgmainuddin.learnwithfun.presentation.home.couses.CourseViewModel
import com.sekhgmainuddin.learnwithfun.presentation.home.enrollCourse.uiStates.GetCourseDetailsState
import com.sekhgmainuddin.learnwithfun.presentation.quiz.QuizActivity
import kotlinx.coroutines.launch
import kotlin.random.Random

class CourseTutorialFragment : BaseFragment() {

    private var _binding: FragmentCourseTutorialBinding? = null
    private val binding: FragmentCourseTutorialBinding
        get() = _binding!!

    private val viewModel by activityViewModels<CourseViewModel>()
    private val args: CourseTutorialFragmentArgs by navArgs()
    private lateinit var courseContentAdapter: CourseContentAdapter
    private lateinit var weeksAdapter: WeeksAdapter
    private val weekList = arrayListOf<Pair<String, Boolean>>()
    private var courseDetailDto: CourseDetailDto? = null
    private var previousVisibleItem = -1

    private var previousClickedItem = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_course_tutorial, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        registerClickListenersAndAdapters()
        if (viewModel.courseDetail.value == null || viewModel.courseDetail.value?._id != args.courseId) {
            viewModel.getCourseDetails(args.courseId)
        }
        bindObservers()
    }

    private fun registerClickListenersAndAdapters() {
        binding.apply {
            backButton.setOnClickListener {
                pressBack()
            }
            weeksAdapter = WeeksAdapter { position ->
                courseContentRecyclerView.smoothScrollToPosition(
                    courseDetailDto!!.weekMap[(position + 1).toString()]!!
                )
            }
            weekRecyclerView.adapter = weeksAdapter
            courseContentAdapter = CourseContentAdapter(
                object : OnCourseContentClickListener {
                    override fun playVideo(contentPosition: Int) {
                        previousClickedItem = contentPosition
                        CourseVideoActivity.player?.release()
                        CourseVideoActivity.player = null
                        startVideoOrQuizForResult.launch(
                            Intent(
                                requireActivity(),
                                CourseVideoActivity::class.java
                            ).putExtra("position", contentPosition)
                                .putExtra("courseDetails", courseDetailDto)
                        )
                    }

                    override fun attendQuiz(contentPosition: Int) {
                        previousClickedItem = contentPosition
                        startVideoOrQuizForResult.launch(
                            Intent(
                                requireActivity(),
                                QuizActivity::class.java
                            ).putExtra("courseId", courseDetailDto!!._id).putExtra(
                                "courseDetails",
                                courseDetailDto!!.contents[contentPosition]
                            )
                        )
                    }

                    override fun downloadNotes(contentPosition: Int) {
                        previousClickedItem = contentPosition
                        Log.d("ClickListeners", "Download Notes $contentPosition")
                    }

                },
            )
            val divider = DividerItemDecoration(
                courseContentRecyclerView.context,
                LinearLayout.VERTICAL
            )
            divider.setDrawable(
                ContextCompat.getDrawable(
                    courseContentRecyclerView.context,
                    R.drawable.vertical_20_dp_space_divider
                )!!
            )
            courseContentRecyclerView.adapter = courseContentAdapter
            courseContentRecyclerView.addItemDecoration(divider)
            val courseContentLayoutManager = LinearLayoutManager(context)
            courseContentRecyclerView.layoutManager = courseContentLayoutManager
            courseContentRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visiblePosition: Int =
                        courseContentLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (visiblePosition > -1 && visiblePosition != previousVisibleItem) {
                        previousVisibleItem = visiblePosition
                        weekRecyclerView.smoothScrollToPosition(courseDetailDto!!.contents[visiblePosition].weekNumber - 1)
                        weeksAdapter.update(courseDetailDto!!.contents[visiblePosition].weekNumber - 1)
                    }
                }
            })
        }
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.courseDetails.collect {
                    hideProgressBar()
                    when (it) {
                        GetCourseDetailsState.Initial -> {}
                        is GetCourseDetailsState.Success -> {
                            courseDetailDto = it.courseDetailDto
                            var firstWeek = true
                            weekList.clear()
                            courseDetailDto?.weekMap?.forEach { w ->
                                weekList.add(Pair(w.key, firstWeek))
                                firstWeek = false
                            }
                            weeksAdapter.submitList(weekList)
                            courseContentAdapter.updateCourseDetails(it.courseDetailDto)
                            courseContentAdapter.submitList(it.courseDetailDto.contents)
                        }

                        GetCourseDetailsState.Loading -> showProgressBar()
                        is GetCourseDetailsState.Error -> {
                            if (it.message.isEmpty()) {
                                showToast(it.messageRes)
                            } else {
                                showToast(it.message)
                            }
                        }
                    }
                }
            }
        }
    }

    val startVideoOrQuizForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == -1) {
                showSnackBar(R.string.course_details_not_found)
            }
            viewModel.getCourseDetails(args.courseId)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}