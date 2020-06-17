package com.parttime.enterprise.uicomman.auth.devicetokenmodels

data class DeviceToken(
    val code: Int,
    val error_message: Any?,
    val message: String,
    val success: Boolean


) {
    override fun toString(): String {
        return "DeviceToken(code=$code, error_message=$error_message, message='$message', success=$success)"
    }
}