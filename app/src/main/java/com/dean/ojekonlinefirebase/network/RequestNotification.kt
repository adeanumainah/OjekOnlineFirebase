package com.dean.ojekonlinefirebase.network

import com.dean.ojekonlinefirebase.model.Booking
import com.google.gson.annotations.SerializedName

class RequestNotification {

    @SerializedName("o")
    var token: String? = null

    @SerializedName("data")
    var sendNotificationModel: Booking? = null
}