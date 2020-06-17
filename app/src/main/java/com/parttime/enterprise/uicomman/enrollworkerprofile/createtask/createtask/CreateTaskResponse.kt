package com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.createtask


import com.google.gson.annotations.SerializedName

data class CreateTaskResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)