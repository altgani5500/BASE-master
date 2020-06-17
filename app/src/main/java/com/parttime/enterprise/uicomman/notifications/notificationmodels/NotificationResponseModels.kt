package com.parttime.enterprise.uicomman.notifications.notificationmodels


import com.google.gson.annotations.SerializedName

data class NotificationResponseModels(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: ArrayList<Message>,
    @SerializedName("success")
    var success: Boolean
)