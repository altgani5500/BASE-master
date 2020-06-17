package com.parttime.enterprise.uicomman.fragments.jobsDetails.models


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("amountPaid")
    var amountPaid: String,
    @SerializedName("applyJob")
    var applyJob: Int,
    @SerializedName("benifits")
    var benifits: String,
    @SerializedName("branchName")
    var branchName: String,
    @SerializedName("createdDate")
    var createdDate: String,
    @SerializedName("currency")
    var currency: String,
    @SerializedName("enrolledParttimeWorker")
    var enrolledParttimeWorker: Int,
    @SerializedName("enterpriseLogo")
    var enterpriseLogo: String,
    @SerializedName("enterpriseName")
    var enterpriseName: String,
    @SerializedName("generealInfo")
    var generealInfo: String,
    @SerializedName("hoursRate")
    var hoursRate: String,
    @SerializedName("industryType")
    var industryType: String,
    @SerializedName("jobDescription")
    var jobDescription: String,
    @SerializedName("jobId")
    var jobId: Int,
    @SerializedName("jobImages")
    var jobImages: ArrayList<JobImage>,
    @SerializedName("jobLat")
    var jobLat: String,
    @SerializedName("jobLocation")
    var jobLocation: String,
    @SerializedName("jobLong")
    var jobLong: String,
    @SerializedName("jobTitle")
    var jobTitle: String,
    @SerializedName("jobType")
    var jobType: String,
    @SerializedName("noOfApplicants")
    var noOfApplicants: String,
    @SerializedName("parttimeType")
    var parttimeType: String,
    @SerializedName("requirements")
    var requirements: String,
    @SerializedName("savedJob")
    var savedJob: Int,
    @SerializedName("totalHoursPerWeek")
    var totalHoursPerWeek: String,
    @SerializedName("workExperience")
    var workExperience: String,
    @SerializedName("workGuidence")
    var workGuidence: String,
    @SerializedName("workingHours")
    var workingHours: String,
    @SerializedName("hourlyRate")
    var hourlyRate: String,
    @SerializedName("enrolledWorker")
    var enrolledWorker: String,
    @SerializedName("hoursePer")
    var hoursePer: String,
    @SerializedName("hoursRates")
    var hoursRates: String
)