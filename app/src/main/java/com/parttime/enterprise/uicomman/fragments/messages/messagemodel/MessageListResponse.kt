package com.parttime.enterprise.uicomman.fragments.messages.messagemodel


import com.google.gson.annotations.SerializedName

data class MessageListResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: List<Message>,
    @SerializedName("success")
    var success: Boolean
)