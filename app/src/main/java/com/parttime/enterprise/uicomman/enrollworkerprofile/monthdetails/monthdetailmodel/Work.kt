package com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel

import com.google.gson.annotations.SerializedName

data class Work(
    @SerializedName("date")
    val date: String,
    @SerializedName("enterpriseStatus")
    val enterpriseStatus: String,
    @SerializedName("lateEarlyHour")
    val lateEarlyHour: Object,
    @SerializedName("punchIn")
    val punchIn: String,
    @SerializedName("punchOut")
    val punchOut: String,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("reasonDoc")
    val reasonDoc: String,
    @SerializedName("schedule")
    val schedule: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("userPunchId")
    val userPunchId: Int,
    @SerializedName("workTime")
    val workTime: Object
)