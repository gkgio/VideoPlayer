package com.gkgio.videoplayer.data

interface BaseTransformer<T, R> {
    fun transform(data: T): R
}