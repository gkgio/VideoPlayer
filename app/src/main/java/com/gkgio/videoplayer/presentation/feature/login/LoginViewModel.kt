package com.gkgio.videoplayer.presentation.feature.login

import com.gkgio.videoplayer.domain.login.LoginRepository
import com.gkgio.videoplayer.domain.login.LoginUseCase
import com.gkgio.videoplayer.domain.video.VideoRepository
import com.gkgio.videoplayer.presentation.SingleLiveEvent
import com.gkgio.videoplayer.presentation.base.BaseViewModel
import com.gkgio.videoplayer.presentation.ext.applySchedulers
import com.gkgio.videoplayer.presentation.feature.video.VideoUrlsTransformer
import com.gkgio.videoplayer.presentation.navigation.Screens
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val router: Router,
    private val loginUseCase: LoginUseCase,
    private val loginRepository: LoginRepository,
    private val videoUrlsTransformer: VideoUrlsTransformer,
    private val videoRepository: VideoRepository
) : BaseViewModel() {

    val errorEvent = SingleLiveEvent<String>()

    fun login(phone: String, carNumber: String) {
        if (phone.isNotEmpty() && carNumber.isNotEmpty()) {
            val instanceId = UUID.randomUUID().toString()
            loginUseCase
                .login(
                    instanceId = instanceId,
                    carNumber = carNumber,
                    phoneNumber = phone
                )
                .map { videoUrlsTransformer.transform(it) }
                .applySchedulers()
                .subscribe({
                    loginRepository.setLoginSuccess(
                        instanceId = instanceId,
                        carNumber = carNumber,
                        phoneNumber = phone
                    )
                    videoRepository.saveVideosUrls(it)
                    router.newRootScreen(Screens.VideoFragmentScreen)
                }, { throwable ->
                    errorEvent.value = throwable.message
                }).addDisposable()
        }
    }
}