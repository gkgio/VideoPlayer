package com.gkgio.videoplayer.presentation.feature.video

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoUrls(
    val urls: List<String>
) : Parcelable