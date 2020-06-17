package com.parttime.enterprise.uicomman.fragments.jobslist.currencyModel


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("currency")
    var currency: String,
    @SerializedName("currencyId")
    var currencyId: Int
)