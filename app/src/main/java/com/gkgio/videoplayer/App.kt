package com.gkgio.videoplayer

import android.app.Application
import com.gkgio.videoplayer.di.AppInjector
import com.gkgio.videoplayer.di.AppModule
import com.gkgio.videoplayer.di.DaggerAppComponent
import io.reactivex.plugins.RxJavaPlugins

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initRxErrorHandler()
    }


    private fun initDagger() {
        val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        AppInjector.appComponent = appComponent
    }

    private fun initRxErrorHandler() {
        RxJavaPlugins.setErrorHandler {
            AppInjector.appComponent.errorReporter.log(it)
            if (BuildConfig.DEBUG) {
                throw it
            }
        }
    }
}