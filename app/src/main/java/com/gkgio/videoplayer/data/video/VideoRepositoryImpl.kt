package com.gkgio.videoplayer.data.video

import android.content.SharedPreferences
import com.gkgio.videoplayer.domain.video.VideoRepository
import com.gkgio.videoplayer.presentation.feature.video.VideoUrls
import com.squareup.moshi.Moshi
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences,
    private val moshi: Moshi
) : VideoRepository {

    companion object {
        const val VIDEO_URLS = "video_urls"
    }

    override fun saveVideosUrls(videoUrls: VideoUrls) {
        val adapter = moshi.adapter(VideoUrls::class.java)
        prefs.edit().putString(VIDEO_URLS, adapter.toJson(videoUrls)).apply()
    }

    override fun loadVideoUrls(): VideoUrls? {
        val jsonString = prefs.getString(VIDEO_URLS, null) ?: return null
        val adapter = moshi.adapter(VideoUrls::class.java)
        return adapter.fromJson(jsonString)
    }
}