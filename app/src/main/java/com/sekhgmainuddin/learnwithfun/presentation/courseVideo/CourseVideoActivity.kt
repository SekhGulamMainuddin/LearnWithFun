package com.sekhgmainuddin.learnwithfun.presentation.courseVideo

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.DividerItemDecoration
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.Constants.CURRENT_VIDEO_START
import com.sekhgmainuddin.learnwithfun.data.remote.bodyParams.UpdateActivityBodyParams
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.ContentDto
import com.sekhgmainuddin.learnwithfun.data.remote.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.databinding.ActivityCourseVideoBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity
import com.sekhgmainuddin.learnwithfun.presentation.courseVideo.adapters.ContentOtherVideosListAdapter
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.AttendExamDialogFragment
import com.sekhgmainuddin.learnwithfun.presentation.home.courseTutorial.uiStates.AttendQuizState
import com.sekhgmainuddin.learnwithfun.presentation.home.searchCourse.CourseViewModel
import com.sekhgmainuddin.learnwithfun.presentation.quiz.QuizActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class CourseVideoActivity : BaseActivity() {

    private lateinit var binding: ActivityCourseVideoBinding
    private var isPortrait = true
    private var currentVideoStart = 0L
    private var currentContent = MutableStateFlow<ContentDto?>(null)
    private lateinit var otherVideosListAdapter: ContentOtherVideosListAdapter
    private var contentPosition = 0
    private var courseDetailDto: CourseDetailDto? = null
    private var otherVideoList = mutableListOf<ContentDto>()
    private val viewModel by viewModels<CourseViewModel>()
    private var startTime: Long = 0

    companion object {
        var player: ExoPlayer? = null
    }

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_video)
        startTime = System.currentTimeMillis()
        isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        if (!isPortrait) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding.lifecycleOwner = this
        binding.content = currentContent
        registerClickListenersAndAdapters()

        courseDetailDto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("courseDetails", CourseDetailDto::class.java)
        } else {
            intent.getParcelableExtra<CourseDetailDto>("courseDetails")
        }
        contentPosition = intent.getIntExtra("position", 0)
        courseDetailDto?.contents?.forEachIndexed { i, c ->
            if (i != contentPosition) {
                otherVideoList.add(c)
            }
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(Activity.RESULT_OK, Intent().putExtra("isChanged", false))
                finish()
            }
        })

        savedInstanceState?.let {
            currentVideoStart = it.getLong(CURRENT_VIDEO_START, 0)
        }

        if (courseDetailDto == null) {
            showToast(R.string.course_details_not_found)
            finish()
        } else {
            refreshLayoutForContentItem(otherVideoList)
            setUpPlayer()
        }
        bindObservers()
    }

    @OptIn(UnstableApi::class)
    private fun registerClickListenersAndAdapters() {
        otherVideosListAdapter =
            ContentOtherVideosListAdapter {
                player?.pause()
                val currentContent = otherVideoList[it]
                val newOtherVideoList = mutableListOf<ContentDto>()
                courseDetailDto?.contents?.forEachIndexed { i, c ->
                    if (currentContent._id == c._id) {
                        this@CourseVideoActivity.contentPosition = i
                    } else {
                        newOtherVideoList.add(c)
                    }
                }
                Log.d(
                    "videoPlay",
                    "playVideo: $contentPosition $currentContent ${otherVideoList.size}"
                )
                refreshLayoutForContentItem(newOtherVideoList)
                otherVideoList.clear()
                otherVideoList.addAll(newOtherVideoList)
            }
        binding.apply {
            otherVideoRecyclerView?.let {
                val divider =
                    DividerItemDecoration(it.context, LinearLayout.HORIZONTAL)
                ContextCompat.getDrawable(it.context, R.drawable.horizontal_20_dp_space_divider)
                    ?.let { it1 -> divider.setDrawable(it1) }
                it.addItemDecoration(divider)
            }
            otherVideoRecyclerView?.adapter = otherVideosListAdapter
            playerView.setShowNextButton(false)
            playerView.setShowPreviousButton(false)
            playerView.setFullscreenButtonClickListener {
                requestedOrientation = if (isPortrait) {
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                isPortrait = !isPortrait
            }
            playQuiz?.setOnClickListener {
                val attendQuiz = AttendExamDialogFragment()
                attendQuiz.arguments = bundleOf(
                    "course" to courseDetailDto,
                    "contentPosition" to contentPosition
                )
                attendQuiz.show(supportFragmentManager, "")
            }
            downloadNotesButton?.setOnClickListener {

            }
        }
    }

    private fun refreshLayoutForContentItem(list: List<ContentDto>) {
        currentContent.value = courseDetailDto?.contents?.get(contentPosition)
        otherVideosListAdapter.submitList(list)
        player?.updateVideoSource()
    }

    private fun bindObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.attendQuiz.collect {
                    if (it is AttendQuizState.Success) {
                        delay(1010)
                        startActivity(
                            Intent(
                                this@CourseVideoActivity,
                                QuizActivity::class.java
                            ).putExtra(
                                "content",
                                courseDetailDto!!.contents[contentPosition]
                            )
                                .putExtra("examId", it.examId)
                                .putExtra("courseId", courseDetailDto!!._id)
                        )
                    }
                }
            }
        }
    }

    @UnstableApi
    private fun setUpPlayer() {
        binding.apply {
            if (player == null) {
                player = ExoPlayer.Builder(this@CourseVideoActivity)
                    .build()
                    .also { exoPlayer ->
                        playerView.player = exoPlayer
                        exoPlayer.updateVideoSource()
                    }
            } else {
                playerView.player = player
                player?.seekTo(currentVideoStart)
            }
        }
    }

    private fun ExoPlayer.updateVideoSource() {
        val url = courseDetailDto?.contents?.get(this@CourseVideoActivity.contentPosition)?.url
        if (url != null) {
            this.removeMediaItem(0)
            val mediaItem = MediaItem.fromUri(url)
            this.setMediaItem(mediaItem, currentVideoStart)
            this.playWhenReady = true
            this.prepare()
        } else {
            showToast(R.string.cannot_play_video_as_source_url_is_null)
        }
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        player?.let {
            outState.putLong(CURRENT_VIDEO_START, it.currentPosition)
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onStop() {
        super.onStop()
        if (startTime != 0L) {
            val currentTime = System.currentTimeMillis()
            viewModel.updateUserActivity(
                UpdateActivityBodyParams(
                    "Video",
                    SimpleDateFormat("dd/MM/yyyy").format(
                        Date(currentTime)
                    ),
                    currentTime - startTime
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.pause()
    }

}