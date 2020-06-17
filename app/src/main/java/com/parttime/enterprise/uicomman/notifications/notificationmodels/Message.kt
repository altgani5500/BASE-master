package com.parttime.enterprise.uicomman.notifications.notificationmodels


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("message")
    var message: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("time")
    var time: String
)