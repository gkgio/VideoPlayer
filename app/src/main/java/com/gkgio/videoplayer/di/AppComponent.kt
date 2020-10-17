package com.gkgio.videoplayer.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.gkgio.videoplayer.presentation.LaunchActivity
import com.gkgio.videoplayer.domain.errorreporter.ErrorReporter
import com.gkgio.videoplayer.presentation.LaunchViewModel
import com.gkgio.videoplayer.presentation.base.BaseFragment
import com.gkgio.videoplayer.presentation.base.BaseViewModel
import com.gkgio.videoplayer.presentation.feature.login.LoginViewModel
import com.gkgio.videoplayer.presentation.feature.video.VideoViewModel
import com.squareup.moshi.Moshi
import dagger.Component
import retrofit2.Retrofit
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AppModule::class,
        NavigationModule::class,
        VideoModule::class,
        LoginModule::class
    ]
)
interface AppComponent {
    fun inject(app: Application)

    fun inject(launchActivity: LaunchActivity)
    fun inject(baseFragment: BaseFragment<BaseViewModel>)

    val launchViewModel: LaunchViewModel
    val videoViewModel: VideoViewModel
    val loginViewModel: LoginViewModel

    val context: Context
    val moshi: Moshi
    val retrofit: Retrofit
    val prefs: SharedPreferences

    val errorReporter: ErrorReporter

    //Cicerone
    val router: Router
    val navigatorHolder: NavigatorHolder
}