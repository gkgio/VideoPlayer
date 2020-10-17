package com.gkgio.videoplayer.data.errorreporter

import com.gkgio.videoplayer.domain.errorreporter.ErrorReporter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class ErrorReporterImpl @Inject constructor() : ErrorReporter {

    override fun log(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}