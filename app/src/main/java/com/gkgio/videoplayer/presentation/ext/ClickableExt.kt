package com.gkgio.videoplayer.presentation.ext

import android.os.SystemClock
import android.view.View

fun View.setDebounceOnClickListener(listener: ((View) -> Unit)?) {
    setOnClickListener(listener?.let { DebounceViewClickListener(it) })
}

class SafeClickHandler(private val listener: ((View) -> Unit)) {
    companion object {
        private const val SAFE_CLICK_DELAY = 200
        var lastClickTime = 0L
        fun isCanHandleClick(currentTime: Long) = lastClickTime < (currentTime - (SAFE_CLICK_DELAY))
    }

    fun handleClick(v: View) {
        val currentTime = SystemClock.elapsedRealtime()
        if (isCanHandleClick(currentTime)) {
            listener(v)
            lastClickTime = currentTime
        }
    }
}

private class DebounceViewClickListener(clickListener: ((View) -> Unit)) : View.OnClickListener {
    private val clickHandler = SafeClickHandler(clickListener)
    override fun onClick(v: View) = clickHandler.handleClick(v)
}