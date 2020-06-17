package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.dialogevolution


import com.google.gson.annotations.SerializedName

data class DialogEvolutionModelRes(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)