package com.gkgio.videoplayer.domain.login

import com.gkgio.videoplayer.domain.video.VideoData
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

interface LoginUseCase {
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

    override fun criticalBatteryLevel(
        instanceId: String,
        carNumber: String,
        phoneNumber: String
    ): Completable =
        loginService.criticalBatteryLevel(
            instanceId,
            carNumber,
            phoneNumber
        )

    override fun shutDown(
        instanceId: String,
        carNumber: String,
        phoneNumber: String
    ): Completable =
        loginService.shutDown(
            instanceId,
            carNumber,
            phoneNumber
        )
}