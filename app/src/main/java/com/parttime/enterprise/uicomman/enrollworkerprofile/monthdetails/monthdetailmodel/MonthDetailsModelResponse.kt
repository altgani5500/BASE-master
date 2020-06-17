package com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel

import com.google.gson.annotations.SerializedName

data class MonthDetailsModelResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("error_message")
    val error_message: Any,
    @SerializedName("message")
    val message: List<Message>,
    @SerializedName("noOfWeek")
    val noOfWeek: Int,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("totalDone")
    val totalDone: Double,
    @SerializedName("totalSchedule")
    val totalSchedule: Int
)