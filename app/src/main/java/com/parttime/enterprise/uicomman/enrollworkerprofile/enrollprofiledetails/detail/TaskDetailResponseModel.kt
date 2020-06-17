package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.detail


import com.google.gson.annotations.SerializedName

data class TaskDetailResponseModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: Message,
    @SerializedName("success")
    var success: Boolean
)