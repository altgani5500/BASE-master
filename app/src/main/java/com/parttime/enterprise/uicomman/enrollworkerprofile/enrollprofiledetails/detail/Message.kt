package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.detail


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("taskDetail")
    var taskDetail: TaskDetail,
    @SerializedName("userDetail")
    var userDetail: UserDetail,
    @SerializedName("workHistory")
    var workHistory: List<Any>
)