package com.parttime.enterprise.uicomman.locationaddress.countrymodel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("country")
    var country: String,
    @SerializedName("isClicked")
    var isClicked: Boolean
)