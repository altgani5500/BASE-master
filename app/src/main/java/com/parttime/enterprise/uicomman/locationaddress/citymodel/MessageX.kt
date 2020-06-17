package com.parttime.enterprise.uicomman.locationaddress.citymodel


import com.google.gson.annotations.SerializedName

data class MessageX(
    @SerializedName("cityList")
    var cityList: ArrayList<City>,
    @SerializedName("stateName")
    var stateName: String
)