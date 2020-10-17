package com.gkgio.videoplayer.data.video

import com.gkgio.videoplayer.data.BaseTransformer
import com.gkgio.videoplayer.domain.video.VideoUrl
import javax.inject.Inject

class VideoUrlTransformer @Inject constructor() :
    BaseTransformer<VideoUrlResponse, VideoUrl> {

    override fun transform(data: VideoUrlResponse) = with(data) {
        VideoUrl(
            id = id,
            url = url
        )
    }
}