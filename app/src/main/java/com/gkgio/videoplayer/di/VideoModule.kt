package com.gkgio.videoplayer.di

import com.gkgio.videoplayer.data.login.LoginRepositoryImpl
import com.gkgio.videoplayer.data.login.LoginServiceImpl
import com.gkgio.videoplayer.data.video.VideoRepositoryImpl
import com.gkgio.videoplayer.domain.video.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create


@Module(includes = [VideoModule.BindsModule::class])
class VideoModule {

    @Module
    abstract inner class BindsModule {

        @Binds
        abstract fun repositoryVideo(arg: VideoRepositoryImpl): VideoRepository
    }
}