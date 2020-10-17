package com.gkgio.videoplayer.di

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class NavigationModule {

    @Provides
    @Singleton
    fun provideCicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    fun provideCiceroneRouter(cicerone: Cicerone<Router>): Router = cicerone.router

    @Provides
    fun provideCiceroneNavigator(cicerone: Cicerone<Router>): NavigatorHolder =
        cicerone.navigatorHolder
}