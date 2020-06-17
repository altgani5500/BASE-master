package com.parttime.enterprise.uicomman.subuser.adduserlist


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("email")
    var email: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("roleId")
    var roleId: String,
    @SerializedName("subUserId")
    var subUserId: Int,
    @SerializedName("subUserLogo")
    var subUserLogo: String
)