package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist


import com.google.gson.annotations.SerializedName

data class WorkerProfileListModelResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: Message,
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("workingHours")
    var workingHours: String,
    @SerializedName("userSchedule")
    var userSchedules: List<UserSchedules>

)