package com.sekhgmainuddin.learnwithfun.presentation.course_video

import android.content.pm.ActivityInfo
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.common.CURRENT_VIDEO_START
import com.sekhgmainuddin.learnwithfun.common.PLAY_WHEN_READY
import com.sekhgmainuddin.learnwithfun.databinding.ActivityCourseVideoBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity

@UnstableApi
class CourseVideoActivity : BaseActivity() {

    private lateinit var binding: ActivityCourseVideoBinding
    private var isPortrait = true
    companion object {
        private var player: ExoPlayer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_video)

        var currentVideoStart = 0L
        var playerWhenReady = false
        savedInstanceState?.let {
            currentVideoStart = it.getLong(CURRENT_VIDEO_START, 0)
            playerWhenReady = it.getBoolean(PLAY_WHEN_READY, false)
        }

        if (player == null) {
            player = ExoPlayer.Builder(this)
                .build()
                .also { exoPlayer ->
                    binding.playerView.player = exoPlayer
                    val mediaItem =
                        MediaItem.fromUri("https://user-images.githubusercontent.com/73953395/246585839-af995eab-02c3-467c-afa1-e05f144a747c.mp4")
                    exoPlayer.setMediaItem(mediaItem, currentVideoStart)
                    exoPlayer.playWhenReady = playerWhenReady
                    exoPlayer.prepare()
                }
        } else {
            binding.playerView.player = player
            player?.seekTo(currentVideoStart)
        }
        binding.playerView.setShowNextButton(false)
        binding.playerView.setShowPreviousButton(false)
        binding.playerView.setFullscreenButtonClickListener {
            requestedOrientation = if (isPortrait) {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            isPortrait = !isPortrait
        }
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        player?.let {
            outState.putLong(CURRENT_VIDEO_START, it.currentPosition)
            outState.putBoolean(PLAY_WHEN_READY, it.playWhenReady)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.pause()
    }

}