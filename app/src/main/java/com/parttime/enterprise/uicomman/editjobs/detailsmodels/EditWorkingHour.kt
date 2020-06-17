package com.parttime.enterprise.uicomman.editjobs.detailsmodels


import com.google.gson.annotations.SerializedName

data class EditWorkingHour(
    @SerializedName("day")
    var day: String,
    @SerializedName("endTime")
    var endTime: String,
    @SerializedName("startTime")
    var startTime: String
)