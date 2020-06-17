package com.parttime.enterprise.uicomman.fragments.jobsDetails.models


import com.google.gson.annotations.SerializedName

data class JobDetailsModelResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: Message,
    @SerializedName("success")
    var success: Boolean
)