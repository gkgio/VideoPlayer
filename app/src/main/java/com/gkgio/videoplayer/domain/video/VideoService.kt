package com.gkgio.videoplayer.domain.video

import io.reactivex.Single

interface VideoService {
    fun loadVideoUrls(instanceId: String, carNumber: String, phoneNumber: String): Single<VideoData>
}