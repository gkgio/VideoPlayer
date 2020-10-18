package com.gkgio.videoplayer.data.login

import com.gkgio.videoplayer.data.video.VideoDataResponse
import com.gkgio.videoplayer.data.video.VideoDataTransformer
import com.gkgio.videoplayer.domain.login.LoginService
import com.gkgio.videoplayer.domain.video.VideoData
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Inject

class LoginServiceImpl @Inject constructor(
    private val loginServiceApi: LoginServiceApi,
    private val videoDataTransformer: VideoDataTransformer
) : LoginService {

    override fun login(
        instanceId: String,
        carNumber: String,
        phoneNumber: String,
        pushToken: String
    ): Single<VideoData> =
        loginServiceApi.getHairSalonData(
            LoginRequest(
                instanceId = instanceId,
                carNumber = carNumber,
                phoneNumber = phoneNumber,
                pushToken = pushToken
            )
        ).map { videoDataTransformer.transform(it) }

    override fun criticalBatteryLevel(
        instanceId: String,
        carNumber: String,
        phoneNumber: String
    ): Completable =
        loginServiceApi.criticalBatteryLevel(
            LoginRequest(
                instanceId = instanceId,
                carNumber = carNumber,
                phoneNumber = phoneNumber
            )
        )

    override fun shutDown(
        instanceId: String,
        carNumber: String,
        phoneNumber: String
    ): Completable =
        loginServiceApi.shutDown(
            LoginRequest(
                instanceId = instanceId,
                carNumber = carNumber,
                phoneNumber = phoneNumber
            )
        )

    interface LoginServiceApi {

        @POST("login")
        fun getHairSalonData(
            @Body request: LoginRequest
        ): Single<VideoDataResponse>

        @POST("login")
        fun criticalBatteryLevel(
            @Body request: LoginRequest
        ): Completable

        @POST("login")
        fun shutDown(
            @Body request: LoginRequest
        ): Completable
    }
}