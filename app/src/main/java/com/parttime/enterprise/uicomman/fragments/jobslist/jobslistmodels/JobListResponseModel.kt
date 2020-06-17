package com.parttime.enterprise.uicomman.fragments.jobslist.jobslistmodels


import com.google.gson.annotations.SerializedName

data class JobListResponseModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: List<Message>,
    @SerializedName("success")
    var success: Boolean
)