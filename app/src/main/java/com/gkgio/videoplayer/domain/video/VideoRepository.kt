package com.gkgio.videoplayer.domain.video

import com.gkgio.videoplayer.presentation.feature.video.VideoUrls

interface VideoRepository {
    fun saveVideosUrls(videoUrls: VideoUrls)
    fun loadVideoUrls(): VideoUrls?
}