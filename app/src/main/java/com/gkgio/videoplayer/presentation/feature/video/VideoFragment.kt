package com.gkgio.videoplayer.presentation.feature.video

import android.os.Bundle
import android.view.View
import com.gkgio.videoplayer.R
import com.gkgio.videoplayer.di.AppInjector
import com.gkgio.videoplayer.presentation.base.BaseFragment
import com.gkgio.videoplayer.presentation.ext.createViewModel
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        videoPlayerView.player = player


    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
    }
}