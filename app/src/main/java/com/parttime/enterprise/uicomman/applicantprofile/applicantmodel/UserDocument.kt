package com.parttime.enterprise.uicomman.applicantprofile.applicantmodel


import com.google.gson.annotations.SerializedName

data class UserDocument(
    /*
    *
    * [{"documentId":1,"documentType":"Resume","documentName":"PALMS Report - Final July 2019.pdf","docUrl":"http:\/\/13.232.62.239\/parttime\/public\/userdocs\/15655878254646.pdf"},{"documentId":2,"documentType":"Resume","documentName":"certificateOne","docUrl":"http:\/\/13.232.62.239\/parttime\/public\/userdocs\/15655929459323.PNG"}]},"error_message":null,"code":200}
    * */

    @SerializedName("docUrl")
    var docUrl: String,
    @SerializedName("documentName")
    var documentName: String,
    @SerializedName("documentId")
    var documentId: Int
)