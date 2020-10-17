package com.gkgio.videoplayer.presentation

import android.content.Context
import com.gkgio.videoplayer.presentation.ext.CacheDataSourceFactory
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File
import javax.inject.Inject

interface ExoPlayerCache {
    fun getCacheDataSourceFactory(): CacheDataSourceFactory
}

class ExoPlayerCacheImpl @Inject constructor(context: Context) : ExoPlayerCache {

    companion object {
        private const val MAX_BYTES: Long =
            30 * 1024 * 1024 // Максимальный размер 1 видео не превышающий 30 мб
        private const val MAX_FILE_SIZE: Long =
            50 * 1024 * 1024 //Максимальный размер общего хранилища
    }

    private val simpleCache = SimpleCache(
        File(context.cacheDir, "media"),
        LeastRecentlyUsedCacheEvictor(MAX_BYTES),
        ExoDatabaseProvider(context)
    )
    private val cacheDataSourceFactory = CacheDataSourceFactory(
        context,
        simpleCache,
        MAX_FILE_SIZE
    )

    override fun getCacheDataSourceFactory() = cacheDataSourceFactory
}