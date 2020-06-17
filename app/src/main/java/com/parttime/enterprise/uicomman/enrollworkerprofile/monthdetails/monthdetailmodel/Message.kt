package com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("work")
    val work: List<Work>,
    @SerializedName("workHourDone")
    val workHourDone: Double,
    @SerializedName("workHourSchedule")
    val workHourSchedule: Int,
    @SerializedName("isClicked")
    var isClicked: Boolean,
    @SerializedName("named")
    var named: String
)