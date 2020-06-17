package com.parttime.enterprise.uicomman.applicantfilter.nationality


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("nationality")
    var nationality: String,
    @SerializedName("nationalityId")
    var nationalityId: Int,
    @SerializedName("isClicked")
    var isClicked: Boolean


)