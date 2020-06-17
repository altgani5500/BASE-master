package com.parttime.enterprise.uicomman.profileedit.indusrtymodel


import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)