package com.gkgio.videoplayer.domain.login

import com.gkgio.videoplayer.domain.video.VideoData
import io.reactivex.Single
import javax.inject.Inject

interface LoginUseCase {
    fun login(
        instanceId: String,
        carNumber: String,
        phoneNumber: String,
        pushToken: String
    ): Single<VideoData>
}

class LoginUseCaseImpl @Inject constructor(
    private val loginService: LoginService
) : LoginUseCase {

    override fun login(
        instanceId: String,
        carNumber: String,
        phoneNumber: String,
        pushToken: String
    ): Single<VideoData> =
        loginService.login(instanceId, carNumber, phoneNumber, pushToken)
}