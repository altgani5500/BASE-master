package com.parttime.enterprise.uicomman.profileedit.indusrtymodel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("category")
    var category: String,
    @SerializedName("categoryId")
    var categoryId: Int
)