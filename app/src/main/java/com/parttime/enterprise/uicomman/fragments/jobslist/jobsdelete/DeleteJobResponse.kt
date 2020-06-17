package com.parttime.enterprise.uicomman.fragments.jobslist.jobsdelete


import com.google.gson.annotations.SerializedName

data class DeleteJobResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: String,
    @SerializedName("message")
    var message: Any?,
    @SerializedName("success")
    var success: Boolean
)