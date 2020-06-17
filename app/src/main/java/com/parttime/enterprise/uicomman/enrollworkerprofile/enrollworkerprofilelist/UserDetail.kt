package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist


import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("jobTitle")
    var jobTitle: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("userId")
    var userId: Int
)