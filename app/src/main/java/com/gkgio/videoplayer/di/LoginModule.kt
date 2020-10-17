package com.gkgio.videoplayer.di

import com.gkgio.videoplayer.data.login.LoginRepositoryImpl
import com.gkgio.videoplayer.data.login.LoginServiceImpl
import com.gkgio.videoplayer.domain.login.LoginRepository
import com.gkgio.videoplayer.domain.login.LoginService
import com.gkgio.videoplayer.domain.login.LoginUseCase
import com.gkgio.videoplayer.domain.login.LoginUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create


@Module(includes = [LoginModule.BindsModule::class])
class LoginModule {

    @Provides
    fun apiLogin(retrofit: Retrofit): LoginServiceImpl.LoginServiceApi = retrofit.create()

    @Module
    abstract inner class BindsModule {

        @Binds
        abstract fun serviceLogin(arg: LoginServiceImpl): LoginService

        @Binds
        abstract fun useCaselogin(arg: LoginUseCaseImpl): LoginUseCase

        @Binds
        abstract fun repositoryLogin(arg: LoginRepositoryImpl): LoginRepository
    }
}