package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist


import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("taskId")
    var taskId: Int,
    @SerializedName("taskName")
    var taskName: String,
    @SerializedName("taskStatus")
    var taskStatus: String,
    @SerializedName("taskType")
    var taskType: Int,
    @SerializedName("evaluation")
    var evaluation: String
)