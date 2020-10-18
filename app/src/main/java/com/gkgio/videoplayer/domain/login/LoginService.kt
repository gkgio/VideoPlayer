package com.gkgio.videoplayer.domain.login

import com.gkgio.videoplayer.domain.video.VideoData
import io.reactivex.Completable
import io.reactivex.Single

interface LoginService {
    fun login(
        instanceId: String,
        carNumber: String,
        phoneNumber: String,
        pushToken: String
    ): Single<VideoData>

    fun criticalBatteryLevel(
        instanceId: String,
        carNumber: String,
        phoneNumber: String
    ): Completable

    fun shutDown(
        instanceId: String,
        carNumber: String,
        phoneNumber: String
    ): Completable
}