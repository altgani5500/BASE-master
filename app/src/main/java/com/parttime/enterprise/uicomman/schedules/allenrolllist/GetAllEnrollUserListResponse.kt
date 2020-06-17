package com.parttime.enterprise.uicomman.schedules.allenrolllist


import com.google.gson.annotations.SerializedName

data class GetAllEnrollUserListResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: List<Message>,
    @SerializedName("success")
    var success: Boolean
)