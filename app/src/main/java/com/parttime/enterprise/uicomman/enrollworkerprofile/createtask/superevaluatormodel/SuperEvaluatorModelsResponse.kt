package com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.superevaluatormodel


import com.google.gson.annotations.SerializedName

data class SuperEvaluatorModelsResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: List<Message>,
    @SerializedName("success")
    var success: Boolean
)