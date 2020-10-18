package com.gkgio.videoplayer.presentation.ext

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import io.reactivex.functions.Function

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applySchedulers(): Single<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.applySchedulers(): Flowable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.applySchedulers(): Completable {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.applySchedulers(): Maybe<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.retryWithDelay(maxRetries: Int, delayInMillis: Long) =
    retryWhen(RetryWithDelayFlowableFunction(maxRetries, delayInMillis))!!

private class RetryWithDelayFlowableFunction(
    private val maxRetries: Int,
    private val delayInMillis: Long
) : Function<Flowable<out Throwable>, Flowable<*>> {

    private var retryCount = 0

    override fun apply(attempts: Flowable<out Throwable>) =
        attempts.flatMap { throwable ->
            if (retryCount++ < maxRetries) Flowable.timer(delayInMillis, TimeUnit.MILLISECONDS)
            else Flowable.error(throwable)
        }!!
}