package com.gkgio.videoplayer.domain.login

import com.gkgio.videoplayer.data.login.LoginRequest

interface LoginRepository {
    fun setLoginSuccess(instanceId: String, carNumber: String, phoneNumber: String)
    fun getLoginData(): LoginRequest?
    fun isLoginSuccess(): Boolean
}