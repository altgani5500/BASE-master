package com.parttime.enterprise.uicomman.editjobs.detailsmodels


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
    @SerializedName("currencyId")
    var currencyId: Int,
    @SerializedName("editWorkingHours")
    var editWorkingHours: List<EditWorkingHour>,
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
    @SerializedName("hoursRates")
    var hoursRates: String,
    @SerializedName("hoursePer")
    var hoursePer: String,
    @SerializedName("industryType")
    var industryType: String,
    @SerializedName("isFlexible")
    var isFlexible: String,
    @SerializedName("isSame")
    var isSame: String,
    @SerializedName("jobDescription")
    var jobDescription: String,
    @SerializedName("jobId")
    var jobId: Int,
    @SerializedName("jobImages")
    var jobImages: List<JobImage>,
    @SerializedName("jobLat")
    var jobLat: String,
    @SerializedName("jobLocation")
    var jobLocation: String,
    @SerializedName("jobLong")
    var jobLong: String,
    @SerializedName("jobStatus")
    var jobStatus: String,
    @SerializedName("jobTitle")
    var jobTitle: String,
    @SerializedName("jobType")
    var jobType: String,
    @SerializedName("max_hours_exp")
    var maxHoursExp: String,
    @SerializedName("min_hours_exp")
    var minHoursExp: String,
    @SerializedName("noOfApplicants")
    var noOfApplicants: String,
    @SerializedName("parttimeType")
    var parttimeType: String,
    @SerializedName("parttimeTypeId")
    var parttimeTypeId: Int,
    @SerializedName("password")
    var password: String,
    @SerializedName("requirements")
    var requirements: String,
    @SerializedName("savedJob")
    var savedJob: Int,
    @SerializedName("totalHoursPerWeek")
    var totalHoursPerWeek: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("workExperience")
    var workExperience: String,
    @SerializedName("workGuidence")
    var workGuidence: String,
    @SerializedName("workingHours")
    var workingHours: String
)