package com.parttime.enterprise.uicomman.applicantfilter.educationdetails


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("educationDetail")
    var educationDetail: String,
    @SerializedName("educationDetailId")
    var educationDetailId: Int,
    @SerializedName("isClicked")
    var isClicked: Boolean
)