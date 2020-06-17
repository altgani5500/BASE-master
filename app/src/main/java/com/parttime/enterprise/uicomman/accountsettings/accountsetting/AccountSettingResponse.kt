package com.parttime.enterprise.uicomman.accountsettings.accountsetting


import com.google.gson.annotations.SerializedName

data class AccountSettingResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)