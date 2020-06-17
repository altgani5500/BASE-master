package com.parttime.enterprise.uicomman.editjobs.detailsmodels


import com.google.gson.annotations.SerializedName

data class EditDetailsData(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: Message,
    @SerializedName("success")
    var success: Boolean
)