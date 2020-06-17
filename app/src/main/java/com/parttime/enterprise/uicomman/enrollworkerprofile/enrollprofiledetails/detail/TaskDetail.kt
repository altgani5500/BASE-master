package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.detail


import com.google.gson.annotations.SerializedName

data class TaskDetail(
    @SerializedName("endTime")
    var endTime: String,
    @SerializedName("startTime")
    var startTime: String,
    @SerializedName("taskDate")
    var taskDate: String,
    @SerializedName("taskDescription")
    var taskDescription: String,
    @SerializedName("taskId")
    var taskId: Int,
    @SerializedName("taskName")
    var taskName: String,
    @SerializedName("taskStatus")
    var taskStatus: String,
    @SerializedName("taskType")
    var taskType: Int,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("evaluation")
    var evaluation:Int,
    @SerializedName("reevaluation")
    var reevaluation:Int,
    @SerializedName("evaluators")
    var evaluators:Int,
    @SerializedName("evaluatorName")
    var evaluatorName:String,
    @SerializedName("taskRating")
    var taskRating:String

)