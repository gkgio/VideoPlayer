package com.gkgio.videoplayer.presentation.feature.video

import com.gkgio.videoplayer.presentation.ExoPlayerCache
import com.gkgio.videoplayer.presentation.base.BaseViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val router: Router,
    private val exoPlayerCache: ExoPlayerCache
) : BaseViewModel() {

}