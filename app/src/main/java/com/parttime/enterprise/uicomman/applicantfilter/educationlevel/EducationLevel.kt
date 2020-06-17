package com.parttime.enterprise.uicomman.applicantfilter.educationlevel


import com.google.gson.annotations.SerializedName

data class EducationLevel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: ArrayList<Message>,
    @SerializedName("success")
    var success: Boolean
)