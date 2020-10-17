package com.gkgio.videoplayer.data.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LoginRequest(
    @Json(name = "instance_id")
    val instanceId: String,
    @Json(name = "car_number")
    val carNumber: String,
    @Json(name = "phone_number")
    val phoneNumber: String,
)