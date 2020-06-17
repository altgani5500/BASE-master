package com.parttime.enterprise.uicomman.fragments.jobslist.jobslistmodels


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("jobApllicants")
    var jobApllicants: Int,
    @SerializedName("jobDescription")
    var jobDescription: String,
    @SerializedName("jobId")
    var jobId: Int,
    @SerializedName("jobLocation")
    var jobLocation: String,
    @SerializedName("jobTitle")
    var jobTitle: String,
    @SerializedName("jobStatus")
    var jobStatus: String
)