package com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels

data class ScheduleTimeResponse(
    var code: Int = 0,
    var error_message: Any? = Any(),
    var message: List<Message> = listOf(),
    var success: Boolean = false
)