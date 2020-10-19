package com.gkgio.videoplayer.presentation

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject

object PushTokenGetting {
    private val subject = SingleSubject.create<String>()

    fun getFirebasePushToken(): Single<String> {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                subject.onError(Throwable("error get token"))
            }

            val token = task.result
            if (token != null) {
                subject.onSuccess(token)
            } else {
                subject.onError(Throwable("error get token"))
            }
        })

        return subject
    }
}