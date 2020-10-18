package com.gkgio.videoplayer.presentation.feature.video

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.gkgio.videoplayer.R
import com.gkgio.videoplayer.di.AppInjector
import com.gkgio.videoplayer.presentation.base.BaseFragment
import com.gkgio.videoplayer.presentation.ext.createViewModel
import com.gkgio.videoplayer.presentation.ext.playVideo
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : BaseFragment<VideoViewModel>(R.layout.fragment_video) {

    private lateinit var player: SimpleExoPlayer

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.videoViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = SimpleExoPlayer.Builder(requireContext()).build()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        videoPlayerView.player = player

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            with(state) {
                if (videosUrls != null && videosUrls.size > currentVideoUrlNumber) {
                    val url = videosUrls[state.currentVideoUrlNumber]
                    player.playVideo(
                        requireContext(),
                        url,
                        exoPlayerCache.getCacheDataSourceFactory(),
                        true
                    )
                }
            }
        })

        player.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if (state == Player.STATE_ENDED) {
                    viewModel.videoFinish()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
        hideSystemUI()
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
        showSystemUI()
    }

    private fun hideSystemUI() {
        activity?.let {
            it.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    private fun showSystemUI() {
        activity?.let {
            it.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}