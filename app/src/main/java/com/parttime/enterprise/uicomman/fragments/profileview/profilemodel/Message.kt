package com.parttime.enterprise.uicomman.fragments.profileview.profilemodel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("enterpriseId")
    var enterpriseId: Int,
    @SerializedName("enterpriseLogo")
    var enterpriseLogo: String,
    @SerializedName("enterpriseName")
    var enterpriseName: String,
    @SerializedName("generalInfo")
    var generalInfo: String,
    @SerializedName("industryType")
    var industryType: String,
    @SerializedName("jobType")
    var jobType: String
)