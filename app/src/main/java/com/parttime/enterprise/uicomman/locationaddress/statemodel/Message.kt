package com.parttime.enterprise.uicomman.locationaddress.statemodel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("state")
    var state: String,
    @SerializedName("isClicked")
    var isClicked: Boolean
)