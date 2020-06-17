package com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.superevaluatormodel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("evaluatorId")
    var evaluatorId: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("isClecked")
    var isClecked: Boolean
)