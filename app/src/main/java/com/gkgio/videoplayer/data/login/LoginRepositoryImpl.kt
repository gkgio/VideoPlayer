package com.gkgio.videoplayer.data.login

import android.content.SharedPreferences
import com.gkgio.videoplayer.data.video.VideoRepositoryImpl
import com.gkgio.videoplayer.domain.login.LoginRepository
import com.gkgio.videoplayer.presentation.feature.video.VideoUrls
import com.squareup.moshi.Moshi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences,
    private val moshi: Moshi
) : LoginRepository {

    companion object {
        const val SUCCESS_LOGIN = "success_login"
        const val DATA_LOGIN = "data_login"
        const val PUSH_TOKEN = "push_token"
    }

    override fun setLoginSuccess(
        instanceId: String,
        carNumber: String,
        phoneNumber: String
    ) {
        val adapter = moshi.adapter(LoginRequest::class.java)
        prefs.edit().putString(
            DATA_LOGIN, adapter.toJson(
                LoginRequest(
                    instanceId = instanceId,
                    carNumber = carNumber,
                    phoneNumber = phoneNumber
                )
            )
        ).apply()

        prefs.edit().putBoolean(SUCCESS_LOGIN, true).apply()
    }

    override fun getLoginData(): LoginRequest? {
        val jsonString = prefs.getString(DATA_LOGIN, null) ?: return null

        val adapter = moshi.adapter(LoginRequest::class.java)
        return adapter.fromJson(jsonString)
    }

    override fun isLoginSuccess(): Boolean =
        prefs.getBoolean(SUCCESS_LOGIN, false)

    override fun savePushToken(token: String) {
        prefs.edit().putString(PUSH_TOKEN, token).apply()
    }

    override fun getPushToken(): String? =
        prefs.getString(PUSH_TOKEN, null)
}