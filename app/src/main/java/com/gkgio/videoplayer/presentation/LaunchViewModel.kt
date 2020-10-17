package com.gkgio.videoplayer.presentation

import com.gkgio.videoplayer.domain.login.LoginRepository
import com.gkgio.videoplayer.presentation.base.BaseViewModel
import com.gkgio.videoplayer.presentation.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LaunchViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val router: Router
) : BaseViewModel() {

    fun onNewStart() {
        if (loginRepository.isLoginSuccess()) {
            router.newRootScreen(Screens.VideoFragmentScreen)
        } else {
            router.newRootScreen(Screens.LoginFragmentScreen)
        }
    }
}