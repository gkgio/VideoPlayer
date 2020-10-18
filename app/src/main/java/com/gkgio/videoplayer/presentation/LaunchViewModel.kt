package com.gkgio.videoplayer.presentation

import com.gkgio.videoplayer.data.video.VideoDataResponse
import com.gkgio.videoplayer.domain.login.LoginRepository
import com.gkgio.videoplayer.domain.login.LoginService
import com.gkgio.videoplayer.presentation.base.BaseViewModel
import com.gkgio.videoplayer.presentation.ext.applySchedulers
import com.gkgio.videoplayer.presentation.feature.video.VideoUrls
import com.gkgio.videoplayer.presentation.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LaunchViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val loginService: LoginService,
    private val router: Router
) : BaseViewModel() {

    fun onNewStart() {
        if (loginRepository.isLoginSuccess()) {
            router.newRootScreen(Screens.VideoFragmentScreen)
        } else {
            router.newRootScreen(Screens.LoginFragmentScreen)
        }
    }

    fun shutDown() {
        loginRepository.getLoginData()?.let { loginData ->
            loginService
                .shutDown(
                    instanceId = loginData.instanceId,
                    carNumber = loginData.carNumber,
                    phoneNumber = loginData.phoneNumber
                )
                .applySchedulers()
                .subscribe({

                }, {

                }).addDisposable()
        }
    }

    fun criticalBatteryLevel() {
        loginRepository.getLoginData()?.let { loginData ->
            loginService
                .criticalBatteryLevel(
                    instanceId = loginData.instanceId,
                    carNumber = loginData.carNumber,
                    phoneNumber = loginData.phoneNumber
                )
                .applySchedulers()
                .subscribe({

                }, {

                }).addDisposable()
        }
    }
}