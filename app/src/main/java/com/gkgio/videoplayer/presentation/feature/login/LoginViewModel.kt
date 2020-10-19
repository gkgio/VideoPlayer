package com.gkgio.videoplayer.presentation.feature.login

import androidx.lifecycle.MutableLiveData
import com.gkgio.videoplayer.domain.login.LoginRepository
import com.gkgio.videoplayer.domain.login.LoginUseCase
import com.gkgio.videoplayer.domain.video.VideoRepository
import com.gkgio.videoplayer.presentation.PushTokenGetting.getFirebasePushToken
import com.gkgio.videoplayer.presentation.SingleLiveEvent
import com.gkgio.videoplayer.presentation.base.BaseViewModel
import com.gkgio.videoplayer.presentation.ext.applySchedulers
import com.gkgio.videoplayer.presentation.feature.video.VideoUrlsTransformer
import com.gkgio.videoplayer.presentation.navigation.Screens
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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
    val pushErrorEvent = SingleLiveEvent<Unit>()
    val progress = MutableLiveData<Boolean>()

    fun login(phone: String, carNumber: String) {
        var pushToken = loginRepository.getPushToken()
        if (pushToken == null) {
            getFirebasePushToken()
                .applySchedulers()
                .subscribe({
                    pushToken = it
                    loginRepository.savePushToken(it)
                    tryToLogin(phone, carNumber, it)
                }, {
                    pushErrorEvent.call()
                }).addDisposable()
        } else {
            pushToken?.let {
                tryToLogin(phone, carNumber, it)
            }
        }
    }

    private fun tryToLogin(phone: String, carNumber: String, pushToken: String) {
        if (phone.isNotEmpty() && carNumber.isNotEmpty()) {
            val instanceId = UUID.randomUUID().toString()
            progress.value = true
            loginUseCase
                .login(
                    instanceId = instanceId,
                    carNumber = carNumber,
                    phoneNumber = phone,
                    pushToken = pushToken
                )
                .map { videoUrlsTransformer.transform(it) }
                .applySchedulers()
                .subscribe({
                    progress.value = false
                    loginRepository.setLoginSuccess(
                        instanceId = instanceId,
                        carNumber = carNumber,
                        phoneNumber = phone
                    )
                    videoRepository.saveVideosUrls(it)
                    router.newRootScreen(Screens.VideoFragmentScreen)
                }, { throwable ->
                    progress.value = false
                    errorEvent.value = throwable.message
                }).addDisposable()
        }
    }
}