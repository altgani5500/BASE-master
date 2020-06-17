package com.parttime.enterprise.uicomman.appsearch.searchresults


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("education")
    var education: String,
    @SerializedName("educationDetail")
    var educationDetail: String,
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("nation")
    var nation: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("userId")
    var userId: Int
)