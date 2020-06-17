package com.parttime.enterprise.uicomman.enrollcalander.schedulecalander


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("endDate")
    var endDate: String,
    @SerializedName("endTime")
    var endTime: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("startDate")
    var startDate: String,
    @SerializedName("startTime")
    var startTime: String,
    @SerializedName("date")
    var date: String,
    @SerializedName("count")
    var count: Int
)