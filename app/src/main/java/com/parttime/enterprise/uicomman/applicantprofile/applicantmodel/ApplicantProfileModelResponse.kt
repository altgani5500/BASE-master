package com.parttime.enterprise.uicomman.applicantprofile.applicantmodel


import com.google.gson.annotations.SerializedName

data class ApplicantProfileModelResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: Message,
    @SerializedName("success")
    var success: Boolean
)


