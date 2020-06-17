package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("taskList")
    var taskList: List<Task>,
    @SerializedName("userDetail")
    var userDetail: UserDetail,
    @SerializedName("workHistory")
    var workHistory: List<Any>
)