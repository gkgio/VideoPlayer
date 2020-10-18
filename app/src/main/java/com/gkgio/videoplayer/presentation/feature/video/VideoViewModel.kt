package com.gkgio.videoplayer.presentation.feature.video

import androidx.lifecycle.MutableLiveData
import com.gkgio.videoplayer.data.login.LoginRequest
import com.gkgio.videoplayer.data.video.VideoDataResponse
import com.gkgio.videoplayer.data.video.VideoDataTransformer
import com.gkgio.videoplayer.domain.login.LoginRepository
import com.gkgio.videoplayer.domain.video.VideoData
import com.gkgio.videoplayer.domain.video.VideoRepository
import com.gkgio.videoplayer.presentation.ExoPlayerCache
import com.gkgio.videoplayer.presentation.NewVideoUrlsEvent
import com.gkgio.videoplayer.presentation.base.BaseViewModel
import com.gkgio.videoplayer.presentation.ext.applySchedulers
import com.gkgio.videoplayer.presentation.ext.nonNullValue
import com.gkgio.videoplayer.presentation.ext.retryWithDelay
import com.gkgio.videoplayer.presentation.navigation.Screens
import com.squareup.moshi.Moshi
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val router: Router,
    private val exoPlayerCache: ExoPlayerCache,
    private val videoRepository: VideoRepository,
    private val videoUrlsTransformer: VideoUrlsTransformer,
    private val newVideoUrlsEvent: NewVideoUrlsEvent,
    private val moshi: Moshi,
    private val videoDataTransformer: VideoDataTransformer
) : BaseViewModel() {

    val state = MutableLiveData<State>()

    init {
        state.value = State(exoPlayerCache = exoPlayerCache)

        val videosUrlsObject = videoRepository.loadVideoUrls()
        if (videosUrlsObject != null) {
            state.value = state.nonNullValue.copy(
                videosUrls = videosUrlsObject.urls
            )
        } else {
            router.newRootScreen(Screens.LoginFragmentScreen)
        }

        newVideoUrlsEvent
            .getEventResult()
            .map {
                val adapter = moshi.adapter(VideoDataResponse::class.java)
                val videoData = adapter.fromJson(it)
                if (videoData != null) {
                    val videoDomainData = videoDataTransformer.transform(videoData)
                    videoUrlsTransformer.transform(videoDomainData)
                } else {
                    VideoUrls(listOf())
                }
            }
            .applySchedulers()
            .subscribe({ videoUrls ->
                if (videoUrls.urls.isNotEmpty()) {
                    videoRepository.saveVideosUrls(videoUrls)
                }
            }, {

            }).addDisposable()
    }

    fun videoFinish() {
        val newVideoNumber = state.nonNullValue.currentVideoUrlNumber + 1
        if (newVideoNumber < state.nonNullValue.videosUrls?.size ?: 0) {
            state.value = state.nonNullValue.copy(currentVideoUrlNumber = newVideoNumber)
        } else {
            state.value = state.nonNullValue.copy(currentVideoUrlNumber = 0)
        }
    }

    data class State(
        val currentVideoUrlNumber: Int = 0,
        val videosUrls: List<String>? = null,
        val exoPlayerCache: ExoPlayerCache
    )
}