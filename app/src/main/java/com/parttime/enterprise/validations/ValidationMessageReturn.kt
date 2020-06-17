package com.parttime.enterprise.validations


import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class ValidationMessageReturn(
    @SerializedName("errMessage")
    var errMessage: String,
    @SerializedName("status")
    var status: Int

) {
    override fun toString(): String {
        return return GsonBuilder().create().toJson(this, ValidationMessageReturn::class.java)
    }
}