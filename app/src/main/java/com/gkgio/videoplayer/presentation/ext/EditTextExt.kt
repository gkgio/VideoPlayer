package com.gkgio.videoplayer.presentation.ext

import android.widget.EditText
import androidx.annotation.DrawableRes

fun EditText.setDrawableResRightIcon(@DrawableRes drawableRes: Int?) {
    drawableRes?.let {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, it, 0)
    } ?: setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
}