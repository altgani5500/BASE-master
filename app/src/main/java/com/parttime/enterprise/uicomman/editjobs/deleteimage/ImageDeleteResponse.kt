package com.parttime.enterprise.uicomman.editjobs.deleteimage


import com.google.gson.annotations.SerializedName

data class ImageDeleteResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)