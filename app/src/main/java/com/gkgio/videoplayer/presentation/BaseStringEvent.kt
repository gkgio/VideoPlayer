package com.gkgio.videoplayer.presentation

import io.reactivex.subjects.PublishSubject

abstract class BaseStringEvent {
    private var subject = PublishSubject.create<String>()

    fun getEventResult() = subject

    open fun onComplete(event: String) = subject.onNext(event)
}