package com.gkgio.videoplayer.presentation.ext

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util

private const val VOLUME_OFF = 0f
private const val VOLUME_ON = 1f

private const val EXO_PLAYER_USER_AGENT = "video-player-android"

var SimpleExoPlayer.isVolumeEnabled: Boolean
    get() = volume != VOLUME_OFF
    set(value) {
        volume = if (value) VOLUME_ON else VOLUME_OFF
    }

fun SimpleExoPlayer.playVideo(
    context: Context,
    url: String,
    cacheDataSourceFactory: CacheDataSourceFactory,
    isVolume: Boolean = true
) = setupPlayer(this, context, url, isVolume, cacheDataSourceFactory)

fun SimpleExoPlayer.playVideo(
    context: Context,
    url: String,
    isVolume: Boolean = true
) = setupPlayer(this, context, url, isVolume)

fun SimpleExoPlayer.stopVideo() {
    playWhenReady = false
    stop()
}

fun SimpleExoPlayer.releaseVideo() {
    stop()
    release()
}

private fun setupPlayer(
    simpleExoPlayer: SimpleExoPlayer,
    context: Context,
    url: String,
    isVolume: Boolean = true,
    cacheDataSourceFactory: CacheDataSourceFactory? = null
) = with(simpleExoPlayer) {
    isVolumeEnabled = isVolume
    repeatMode = Player.REPEAT_MODE_ALL
    prepare(currentPrepare(context, url, cacheDataSourceFactory))
    playWhenReady = true
}

private fun currentPrepare(
    context: Context,
    url: String,
    cacheDataSourceFactory: CacheDataSourceFactory? = null
) = cacheDataSourceFactory?.let { getCacheMediaSource(url, it) } ?: getMediaSource(url, context)

private fun getMediaSource(url: String, context: Context) = ProgressiveMediaSource
    .Factory(DefaultHttpDataSourceFactory(Util.getUserAgent(context, EXO_PLAYER_USER_AGENT)))
    .createMediaSource(Uri.parse(url))

private fun getCacheMediaSource(
    url: String,
    cacheDataSourceFactory: CacheDataSourceFactory
) = ProgressiveMediaSource
    .Factory(cacheDataSourceFactory)
    .createMediaSource(Uri.parse(url))

class CacheDataSourceFactory(
    context: Context,
    private val simpleCache: SimpleCache,
    private val maxFileSize: Long
) : DataSource.Factory {
    private val bandWidthMeter = DefaultBandwidthMeter.Builder(context).build()
    private val defaultDataSourceFactory =
        DefaultDataSourceFactory(
            context,
            bandWidthMeter,
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(context, EXO_PLAYER_USER_AGENT),
                bandWidthMeter
            )
        )

    override fun createDataSource() = CacheDataSource(
        simpleCache,
        defaultDataSourceFactory.createDataSource(),
        FileDataSource(),
        CacheDataSink(simpleCache, maxFileSize),
        CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
        null
    )
}
