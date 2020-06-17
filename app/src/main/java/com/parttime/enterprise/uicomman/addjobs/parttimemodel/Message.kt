package com.parttime.enterprise.uicomman.addjobs.parttimemodel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("parttimeTypeId")
    var parttimeTypeId: Int,
    @SerializedName("type")
    var type: String
)