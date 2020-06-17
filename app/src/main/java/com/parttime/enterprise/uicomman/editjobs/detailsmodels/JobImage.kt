package com.parttime.enterprise.uicomman.editjobs.detailsmodels


import com.google.gson.annotations.SerializedName

data class JobImage(
    @SerializedName("jobImage")
    var jobImage: String,
    @SerializedName("jobImageId")
    var jobImageId: Int
)