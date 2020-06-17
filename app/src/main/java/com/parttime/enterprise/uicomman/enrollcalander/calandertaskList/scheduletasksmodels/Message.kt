package com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels

data class Message(
    var time: String = "",
    var userList: List<User> = listOf()
)