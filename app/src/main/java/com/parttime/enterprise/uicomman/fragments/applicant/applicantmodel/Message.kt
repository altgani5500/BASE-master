package com.parttime.enterprise.uicomman.fragments.applicant.applicantmodel


import com.google.gson.annotations.SerializedName

data class Message(

    @SerializedName("appliedOn")
    var appliedOn: String,
    @SerializedName("education")
    var education: String,
    @SerializedName("jobApplyId")
    var jobApplyId: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("nation")
    var nation: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("workExperience")
    var workExperience: Int,
    @SerializedName("applicantId")
    var applicantId: Int

//{"success":true,"message":[{"nation":"American","education":"","jobApplyId":37,"status":"Pending","applicantId":12,"name":"Shreee Pathakk","profilePicture":"http:\/\/13.232.62.239\/parttime\/public\/userimage\/1563519571.jpg","workExperience":0,"appliedOn":"24 Jul 2019"}],"error_message":null,"code":200}
)