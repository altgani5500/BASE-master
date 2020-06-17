package com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels


data class User(
    var jobTitle: String = "",
    var name: String = "",
    var prifilePicture: String = "",
    var scheduleTimingId: Int = 0,
    var startTime: String = "",
    var endTime: String = "",
    var scheduleDate: String = ""

    )