package com.parttime.enterprise.uicomman.auth.authmodels

data class AuthLoginModel(
    val code: Int,
    val error_message: Any?,
    val message: Message,
    val success: Boolean
)