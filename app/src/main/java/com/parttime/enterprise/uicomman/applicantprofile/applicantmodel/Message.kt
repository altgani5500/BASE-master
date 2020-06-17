package com.parttime.enterprise.uicomman.applicantprofile.applicantmodel


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Message(
    @SerializedName("age")
    var age: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("branchName")
    var branchName: String,
    @SerializedName("companyName")
    var companyName: String,
    @SerializedName("education")
    var education: String,
    @SerializedName("educationDetail")
    var educationDetail: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("nation")
    var nation: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("userDocuments")
    var userDocuments: List<UserDocument>,
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("userLat")
    var userLat: String,
    @SerializedName("userLong")
    var userLong: String,
    @SerializedName("userSetting")
    var userSeting: UserSettingProfile,
    @SerializedName("workHistory")
    var workHistory: WorkHistoryModel,
    @SerializedName("WorkHistoryList")
    var workHistoryList: ArrayList<WorkHistoryList>,
    @SerializedName("userTaskList")
    var userTaskList: ArrayList<UserTaskList>,
    @SerializedName("isUserTaskShow")
    var isUserTaskShow: Int

):Serializable

data class UserTaskList(
    @SerializedName("enterpriseName")
    var enterpriseName: String,
    @SerializedName("taskName")
    var taskName: String,
    @SerializedName("taskStatus")
    var taskStatus: String,
    @SerializedName("taskType")
    var taskType: String,
    @SerializedName("reason")
    var reason: String,
    @SerializedName("taskRating")
    var taskRating: String,
    @SerializedName("evaluatorName")
    var evaluatorName: String,
    @SerializedName("evaluation")
    var evaluation: String
):Serializable

data class WorkHistoryList(
    @SerializedName("enrolledUserId")
    var enrolledUserId: Int,
    @SerializedName("jobTitle")
    var jobTitle: String,
    @SerializedName("enterpriseName")
    var enterpriseName: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("jobJoining")
    var jobJoining: String,
    @SerializedName("totalWorks")
    var totalWorks: Object,
    @SerializedName("schedule")
    var schedule: Object,
    @SerializedName("isWorking")
    var isWorking: Int
):Serializable

data class UserSettingProfile(
    @SerializedName("cv")
    var cv: Int,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("location")
    var locations: Int,
    @SerializedName("education_level")
    var education_level: Int,
    @SerializedName("age")
    var age: Int,
    @SerializedName("first_name")
    var first_name: Int,
    @SerializedName("last_name")
    var last_name: Int,
    @SerializedName("complete_profile")
    var complete_profile: Int,
    @SerializedName("work_hours")
    var work_hours: Int,
    @SerializedName("isApplicant ")
    var isApplicant : Int


)