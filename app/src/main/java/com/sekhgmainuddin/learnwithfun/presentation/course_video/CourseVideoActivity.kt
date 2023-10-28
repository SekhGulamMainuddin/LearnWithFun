package com.sekhgmainuddin.learnwithfun.presentation.course_video

import android.content.pm.ActivityInfo
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.sekhgmainuddin.learnwithfun.R
import com.sekhgmainuddin.learnwithfun.databinding.ActivityCourseVideoBinding
import com.sekhgmainuddin.learnwithfun.presentation.base.BaseActivity

var currentVideoStart = 0L

@UnstableApi
class CourseVideoActivity : BaseActivity() {

    private lateinit var binding: ActivityCourseVideoBinding
    private var isPortrait = true
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_video)

        if (player == null) {
            Toast.makeText(this, "YES CREATED", Toast.LENGTH_SHORT).show()
            player = ExoPlayer.Builder(this)
                .build()
                .also { exoPlayer ->
                    binding.playerView.player = exoPlayer
                    val mediaItem =
                        MediaItem.fromUri("https://user-images.githubusercontent.com/73953395/246585839-af995eab-02c3-467c-afa1-e05f144a747c.mp4")
                    exoPlayer.setMediaItem(mediaItem, currentVideoStart)
                    exoPlayer.playWhenReady = true
                    exoPlayer.prepare()
                }
        }else{
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

    private fun pausePlayer() = player?.let {
        currentVideoStart = it.currentPosition
        it.pause()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

}