package com.parttime.enterprise.uicomman.homes.homesmodels


import com.google.gson.annotations.SerializedName

data class HomesModels(
    @SerializedName("code")
    var code: Int,
    @SerializedName("enrolledWorker")
    var enrolledWorker: String,
    @SerializedName("enterpriseRole")
    var enterpriseRole: List<String>,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("hourlyRate")
    var hourlyRate: String,
    @SerializedName("isNotification")
    var isNotification: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("accountType")
    var accountType: Int
    )