package com.gkgio.videoplayer.domain.errorreporter

interface ErrorReporter {
    fun log(throwable: Throwable)
}