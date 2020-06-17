package com.parttime.enterprise.uicomman.fragments.enroll.workesmodels.response


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("branchName")
    var branchName: String,
    @SerializedName("enrolledUsers")
    var enrolledUsers: List<EnrolledUser>
)