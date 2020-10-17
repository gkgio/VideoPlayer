package com.gkgio.videoplayer.data.video

import com.gkgio.videoplayer.data.login.LoginRequest
import com.gkgio.videoplayer.data.video.VideoDataResponse
import com.gkgio.videoplayer.data.video.VideoDataTransformer
import com.gkgio.videoplayer.domain.login.LoginService
import com.gkgio.videoplayer.domain.video.VideoData
import com.gkgio.videoplayer.domain.video.VideoService
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Inject

class VideoServiceImpl @Inject constructor(
    private val videoServiceApi: VideoServiceApi,
    private val videoDataTransformer: VideoDataTransformer
) : VideoService {

    override fun loadVideoUrls(
        instanceId: String,
        carNumber: String,
        phoneNumber: String
    ): Single<VideoData> =
        videoServiceApi.getHairSalonData(
            LoginRequest(
                instanceId = instanceId,
                carNumber = carNumber,
                phoneNumber = phoneNumber
            )
        ).map { videoDataTransformer.transform(it) }

    interface VideoServiceApi {

        @POST("updateVideo")
        fun getHairSalonData(
            @Body request: LoginRequest
        ): Single<VideoDataResponse>
    }
}