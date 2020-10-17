package com.gkgio.videoplayer.data.video

import com.gkgio.videoplayer.data.BaseTransformer
import com.gkgio.videoplayer.domain.video.VideoData
import javax.inject.Inject

class VideoDataTransformer @Inject constructor(
    private val videoUrlTransformer: VideoUrlTransformer
) : BaseTransformer<VideoDataResponse, VideoData> {

    override fun transform(data: VideoDataResponse) = with(data) {
        VideoData(
            videoUrls = videoUrls.map { videoUrlTransformer.transform(it) },
            algorithm = algorithm
        )
    }
}