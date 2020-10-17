package com.gkgio.videoplayer.presentation.navigation

import com.gkgio.videoplayer.presentation.feature.login.LoginFragment
import com.gkgio.videoplayer.presentation.feature.video.VideoFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen


object Screens {

    object VideoFragmentScreen : SupportAppScreen() {
        override fun getFragment() = VideoFragment()
    }

    object LoginFragmentScreen : SupportAppScreen() {
        override fun getFragment() = LoginFragment()
    }

}