package com.parttime.enterprise.uicomman.schedules.allenrolllist


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("jobTitle")
    var jobTitle: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("isClicked")
    var isClicked: Boolean

)