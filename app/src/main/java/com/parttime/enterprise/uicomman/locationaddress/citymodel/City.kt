package com.parttime.enterprise.uicomman.locationaddress.citymodel


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("city")
    var city: String,
    @SerializedName("isClicked")
    var isClicked: Boolean,
    @SerializedName("stateNames")
    var stateNames: String
)