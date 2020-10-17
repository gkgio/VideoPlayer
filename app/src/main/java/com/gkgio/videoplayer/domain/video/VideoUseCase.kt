package com.gkgio.videoplayer.domain.video

import io.reactivex.Single
import javax.inject.Inject

interface VideoUseCase {
    fun loadVideoUrls(instanceId: String, carNumber: String, phoneNumber: String): Single<VideoData>
}

class VideoUseCaseImpl @Inject constructor(
    private val videoService: VideoService
) : VideoUseCase {

    override fun loadVideoUrls(
        instanceId: String,
        carNumber: String,
        phoneNumber: String
    ): Single<VideoData> =
        videoService.loadVideoUrls(instanceId, carNumber, phoneNumber)
}

