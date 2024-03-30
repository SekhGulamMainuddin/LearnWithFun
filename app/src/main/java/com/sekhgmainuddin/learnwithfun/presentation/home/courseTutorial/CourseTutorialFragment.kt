package com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial

import android.content.Intent
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.databinding.FragmentCourseTutorialBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseFragment
import com.sekhgmainuddin.learnwithfun.presentation.courseVideo.CourseVideoActivity
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.CourseContentAdapter
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.OnCourseContentClickListener
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.adapters.WeeksAdapter
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.uiStates.AttendQuizState
import com.sekhgmainuddin.learnwithfun.presentation.home.enrollCourse.uiStates.GetCourseDetailsState
import com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse.CourseViewModel
import com.sekhgmainuddin.learnwithfun.presentation.quiz.QuizActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    private var requestedRefresh = false
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
            refreshButton.setOnClickListener {
                swipeRefreshLayout.isRefreshing = true
                this@CourseTutorialFragment.viewModel.getCourseDetails(args.courseId)
            }
            swipeRefreshLayout.setOnRefreshListener {
                requestedRefresh = true
                this@CourseTutorialFragment.viewModel.getCourseDetails(args.courseId)
            }
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
                        if (!courseDetailDto!!.courseCoverage!!.quizAttended.containsKey(
                                courseDetailDto!!.contents[contentPosition]._id
                            )
                        ) {
                            findNavController().navigate(
                                CourseTutorialFragmentDirections.actionCourseTutorialFragmentToAttendExamDialog(
                                    course = courseDetailDto!!,
                                    contentPosition = contentPosition
                                )
                            )
                        } else {
                            showSnackBar(R.string.quiz_already_attended)
                        }
                    }

                    override fun downloadNotes(contentPosition: Int) {
                        previousClickedItem = contentPosition
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
            launch {
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
                                binding.swipeRefreshLayout.isRefreshing = false
                                val quizPosition = args.defaultScrollPosition-1
                                var scrollPosition = 0
                                if (quizPosition>=0 && !requestedRefresh){
                                    for (i in 0..it.courseDetailDto.contents.size) {
                                        if(!it.courseDetailDto.contents[i].quiz.isNullOrEmpty()) {
                                            if(i > quizPosition) {
                                                scrollPosition = i
                                                break
                                            }
                                        }
                                    }
                                    binding.courseContentRecyclerView.smoothScrollToPosition(scrollPosition)
                                }
                            }

                            GetCourseDetailsState.Loading -> showProgressBar()
                            is GetCourseDetailsState.Error -> {
                                binding.swipeRefreshLayout.isRefreshing = false
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
            launch {
                viewModel.attendQuiz.collect {
                    if (it is AttendQuizState.Success) {
                        delay(1010)
                        startActivity(
                            Intent(
                                requireActivity(),
                                QuizActivity::class.java
                            ).putExtra(
                                "content",
                                courseDetailDto!!.contents[previousClickedItem]
                            )
                                .putExtra("examId", it.examId)
                                .putExtra("courseId", courseDetailDto!!._id)
                        )
                    }
                }
            }
        }
    }

    val startVideoOrQuizForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == -101) {
                showSnackBar(R.string.course_details_not_found)
            }
            viewModel.getCourseDetails(args.courseId)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}