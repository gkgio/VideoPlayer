package com.gkgio.videoplayer.presentation.feature.video

import com.gkgio.videoplayer.data.BaseTransformer
import com.gkgio.videoplayer.data.video.VideoUrlResponse
import com.gkgio.videoplayer.domain.video.VideoData
import com.gkgio.videoplayer.domain.video.VideoUrl
import javax.inject.Inject

class VideoUrlsTransformer @Inject constructor() :
    BaseTransformer<VideoData, VideoUrls> {

    override fun transform(data: VideoData) = with(data) {
        VideoUrls(
            urls = sortVideoUrls(data)
        )
    }

    private fun sortVideoUrls(videoData: VideoData): List<String> {
        val videoUrlsObjects = videoData.videoUrls
        val algorithm = videoData.algorithm
        val videoUrls = mutableListOf<String>()
        algorithm.forEach { urlId ->
            videoUrlsObjects.find { it.id == urlId }?.let {
                videoUrls.add(it.url)
            }
        }
        return videoUrls
    }
}