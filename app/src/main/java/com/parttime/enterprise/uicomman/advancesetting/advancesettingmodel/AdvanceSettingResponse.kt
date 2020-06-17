package com.parttime.enterprise.uicomman.advancesetting.advancesettingmodel


import com.google.gson.annotations.SerializedName

data class AdvanceSettingResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)