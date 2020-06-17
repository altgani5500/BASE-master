package com.parttime.enterprise.uicomman.applicantprofile.applicantmodel

import com.google.gson.annotations.SerializedName


data class WorkHistoryModel (
    @SerializedName("allTask")
    var allTask: Int,
    @SerializedName("pending")
    var pending: Int,
    @SerializedName("inprocess")
    var inprocess: Int,
    @SerializedName("completed")
    var completed: Int,
    @SerializedName("userEnroll")
    var userEnroll: Int,
    @SerializedName("workDone")
    var workDone: Object
)
