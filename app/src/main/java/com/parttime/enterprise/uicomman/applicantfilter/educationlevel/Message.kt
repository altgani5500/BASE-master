package com.parttime.enterprise.uicomman.applicantfilter.educationlevel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("education")
    var education: String,
    @SerializedName("educationId")
    var educationId: Int,
    @SerializedName("isClicked")
    var isClicked: Boolean

)