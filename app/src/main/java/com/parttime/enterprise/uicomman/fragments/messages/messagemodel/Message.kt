package com.parttime.enterprise.uicomman.fragments.messages.messagemodel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("isRead")
    var isRead: Int,
    @SerializedName("lastMessage")
    var lastMessage: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("profilePicture")
    var profilePicture: String,
    @SerializedName("receiverId")
    var receiverId: Int,
    @SerializedName("senderId")
    var senderId: Int,
    @SerializedName("time")
    var time: String,
    @SerializedName("unreadCount")
    var unreadCount: Int
)