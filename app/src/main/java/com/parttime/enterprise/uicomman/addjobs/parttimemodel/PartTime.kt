package com.parttime.enterprise.uicomman.addjobs.parttimemodel


import com.google.gson.annotations.SerializedName

data class PartTime(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: List<Message>,
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("jobtitle")
    var jobTitle: List<Any?>
)