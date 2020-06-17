package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist

import com.google.gson.annotations.SerializedName

data class UserSchedules (
    @SerializedName("date")
    var dates: String,
    @SerializedName("schedule")
    var schedule: String,
    @SerializedName("punchTime")
    var punchTime: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("isClicked")
    var isClicked: Boolean

)