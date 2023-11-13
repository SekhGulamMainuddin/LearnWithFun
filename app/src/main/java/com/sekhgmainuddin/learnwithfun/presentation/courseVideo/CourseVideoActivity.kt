package com.sekhgmainuddin.learnwithfun.presentation.courseVideo

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.DividerItemDecoration
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.Constants.CURRENT_VIDEO_START
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.ContentDto
import com.sekhgmainuddin.learnwithfun.data.dto.courseDetails.CourseDetailDto
import com.sekhgmainuddin.learnwithfun.databinding.ActivityCourseVideoBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity
import com.sekhgmainuddin.learnwithfun.presentation.courseVideo.adapters.ContentOtherVideosListAdapter
import com.sekhgmainuddin.learnwithfun.presentation.quiz.QuizActivity
import kotlinx.coroutines.flow.MutableStateFlow

class CourseVideoActivity : BaseActivity() {

    private lateinit var binding: ActivityCourseVideoBinding
    private var isPortrait = true
    private var currentVideoStart = 0L
    private var currentContent = MutableStateFlow<ContentDto?>(null)
    private lateinit var otherVideosListAdapter: ContentOtherVideosListAdapter
    private var contentPosition = 0
    private var courseDetailDto: CourseDetailDto? = null
    private var otherVideoList = mutableListOf<ContentDto>()

    companion object {
        var player: ExoPlayer? = null
    }

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_video)

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
        } else {
            refreshLayoutForContentItem(otherVideoList)
            setUpPlayer()
        }
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
                startActivity(Intent(this@CourseVideoActivity, QuizActivity::class.java))
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

    override fun onDestroy() {
        super.onDestroy()
        player?.pause()
    }

}