package com.gkgio.videoplayer.data.login

import com.gkgio.videoplayer.data.video.VideoDataResponse
import com.gkgio.videoplayer.data.video.VideoDataTransformer
import com.gkgio.videoplayer.domain.login.LoginService
import com.gkgio.videoplayer.domain.video.VideoData
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
        phoneNumber: String
    ): Single<VideoData> =
        loginServiceApi.getHairSalonData(
            LoginRequest(
                instanceId = instanceId,
                carNumber = carNumber,
                phoneNumber = phoneNumber
            )
        ).map { videoDataTransformer.transform(it) }

    interface LoginServiceApi {

        @POST("login")
        fun getHairSalonData(
            @Body request: LoginRequest
        ): Single<VideoDataResponse>
    }
}