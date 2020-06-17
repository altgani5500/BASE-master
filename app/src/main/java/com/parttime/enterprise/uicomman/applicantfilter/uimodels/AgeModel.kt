package com.parttime.enterprise.uicomman.applicantfilter.uimodels


import com.google.gson.annotations.SerializedName

data class AgeModel(
    @SerializedName("name")
    var name: String,
    @SerializedName("isClicked")
    var isClick: Boolean
)