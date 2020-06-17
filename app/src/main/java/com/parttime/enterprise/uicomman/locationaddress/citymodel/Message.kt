package com.parttime.enterprise.uicomman.locationaddress.citymodel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("city")
    var city: String,
    @SerializedName("isClicked")
    var isClicked: Boolean
)