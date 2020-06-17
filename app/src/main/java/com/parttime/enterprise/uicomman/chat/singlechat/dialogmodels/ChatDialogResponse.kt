package com.parttime.enterprise.uicomman.chat.singlechat.dialogmodels


import com.google.gson.annotations.SerializedName

data class ChatDialogResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("error_message")
    var errorMessage: Any?,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("request")
    var request:RequestResponse

)

data class RequestResponse(
    @SerializedName("sender_id")
    var sender_id:String,
    @SerializedName("receiver_id")
    var receiver_id:String,
    @SerializedName("sender_type")
    var sender_type:String,
    @SerializedName("receiver_type")
    var receiver_type:String,
    @SerializedName("message")
    var message:String,
    @SerializedName("start_time")
    var start_time:String,
    @SerializedName("end_time")
    var end_time:String,
    @SerializedName("date")
    var date:String,
    @SerializedName("message_type")
    var message_type:String,
    @SerializedName("new_created_at")
    var new_created_at:String,
    @SerializedName("chat_id")
    var chat_id:Int

    )