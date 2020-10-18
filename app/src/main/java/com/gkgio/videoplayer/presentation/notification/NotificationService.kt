package com.gkgio.videoplayer.presentation.notification

import com.gkgio.videoplayer.domain.login.LoginRepository
import com.gkgio.videoplayer.presentation.NewVideoUrlsEvent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class NotificationService @Inject constructor(
    private val newVideoUrlsEvent: NewVideoUrlsEvent,
    private val loginRepository: LoginRepository
) : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        loginRepository.savePushToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val dataVideo = remoteMessage.data["video_data"]
        dataVideo?.let {
            newVideoUrlsEvent.onComplete(it)
        }
    }
}