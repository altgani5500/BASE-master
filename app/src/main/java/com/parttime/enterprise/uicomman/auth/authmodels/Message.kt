package com.parttime.enterprise.uicomman.auth.authmodels

data class Message(
    val apiToken: String,
    val email: String,
    val enterpriseId: Int,
    val name: String,
    val socialProvider: String
)