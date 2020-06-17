package com.parttime.enterprise.uicomman.fragments.enroll.workesmodels.response


import com.google.gson.annotations.SerializedName

data class EnrolledUser(
    @SerializedName("enrolledAt")
    var enrolledAt: String,
    @SerializedName("jobTitle")
    var jobTitle: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("workNetwork")
    var workNetwork: Int
)