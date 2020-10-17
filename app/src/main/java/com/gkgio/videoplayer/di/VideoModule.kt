package com.gkgio.videoplayer.di

import com.gkgio.videoplayer.data.login.LoginRepositoryImpl
import com.gkgio.videoplayer.data.login.LoginServiceImpl
import com.gkgio.videoplayer.data.video.VideoRepositoryImpl
import com.gkgio.videoplayer.data.video.VideoServiceImpl
import com.gkgio.videoplayer.domain.login.LoginRepository
import com.gkgio.videoplayer.domain.login.LoginService
import com.gkgio.videoplayer.domain.login.LoginUseCase
import com.gkgio.videoplayer.domain.login.LoginUseCaseImpl
import com.gkgio.videoplayer.domain.video.VideoRepository
import com.gkgio.videoplayer.domain.video.VideoService
import com.gkgio.videoplayer.domain.video.VideoUseCase
import com.gkgio.videoplayer.domain.video.VideoUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create


@Module(includes = [VideoModule.BindsModule::class])
class VideoModule {

    @Provides
    fun apiVideo(retrofit: Retrofit): VideoServiceImpl.VideoServiceApi = retrofit.create()

    @Module
    abstract inner class BindsModule {

        @Binds
        abstract fun serviceVideo(arg: VideoServiceImpl): VideoService

        @Binds
        abstract fun useCaseVideo(arg: VideoUseCaseImpl): VideoUseCase

        @Binds
        abstract fun repositoryVideo(arg: VideoRepositoryImpl): VideoRepository
    }
}