package com.parttime.enterprise.uicomman.fragments.applicant.acceptfinal


import com.google.gson.annotations.SerializedName

data class AcceptInterviewResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)