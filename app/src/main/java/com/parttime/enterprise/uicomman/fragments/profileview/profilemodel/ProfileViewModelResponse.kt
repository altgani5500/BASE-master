package com.parttime.enterprise.uicomman.fragments.profileview.profilemodel


import com.google.gson.annotations.SerializedName

data class ProfileViewModelResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: Message,
    @SerializedName("success")
    var success: Boolean
)