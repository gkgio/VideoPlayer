package com.gkgio.videoplayer.data

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceInterceptor @Inject constructor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Костыль для бэкенда
        val timestamp = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())).toString()

        // Фикс некорректной кодировки ретрофит и окхттп https://github.com/square/retrofit/issues/1199
        val newUrl = originalRequest.url.toString()
            .replace("%26", "&")
            .replace("%3D", "=")

        val requestBuilder = originalRequest.newBuilder()
            .url(newUrl)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", getUserAgent())
            .addHeader("X-Auth-Timestamp", timestamp)
            .method(originalRequest.method, originalRequest.body)

        return chain.proceed(requestBuilder.build())
    }

    private fun getUserAgent(): String {
        return "VideoPlayer" +
                getVersionName() +
                " (Android ${Build.VERSION.RELEASE}" +
                "; Scale/${context.resources.displayMetrics.density})"
    }

    private fun getVersionName(): String {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {

        }
        return "1.0"
    }
}