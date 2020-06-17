package com.parttime.enterprise.validations

import android.app.Activity
import com.parttime.enterprise.R

class ChatCalanderValidation {
    fun isInputValida(
        context: Activity,
        date1: String,
        startHour: String,
        startTime: String,
        endHour: String,
        endTime: String,
        userInputMsg: String
    ): ValidationMessageReturn {
        if (date1.isNotEmpty()) {
            if (startHour.isNotEmpty()) {
                if (endHour.isNotEmpty()) {
                    if (startTime.isNotEmpty()) {
                        if (endTime.isNotEmpty()) {
                            if (!startHour.contentEquals(endHour) || (startHour.contentEquals(
                                    endHour
                                ) && !endTime.contentEquals(startTime))
                            ) {
                                if (userInputMsg.isNotEmpty() && userInputMsg.length > 2) {
                                    return ValidationMessageReturn(status = 1, errMessage = "")
                                } else {
                                    return ValidationMessageReturn(
                                        status = 0,
                                        errMessage = context.getString(R.string.type_message)
                                    )
                                }
                            } else {
                                return ValidationMessageReturn(
                                    status = 0,
                                    errMessage = context.getString(R.string.select_date_time_same)
                                )
                            }
                        } else {
                            return ValidationMessageReturn(
                                status = 0,
                                errMessage = context.getString(R.string.end_time)
                            )
                        }
                    } else {
                        return ValidationMessageReturn(
                            status = 0,
                            errMessage = context.getString(R.string.start_time)
                        )
                    }
                } else {
                    return ValidationMessageReturn(
                        status = 0,
                        errMessage = context.getString(R.string.end_time)
                    )
                }
            } else {
                return ValidationMessageReturn(
                    status = 0,
                    errMessage = context.getString(R.string.select_start_time)
                )
            }
        } else {
            return ValidationMessageReturn(
                status = 0,
                errMessage = context.getString(R.string.select_date)
            )
        }
        //  return  ValidationMessageReturn(status = 0,errMessage ="" )

    }

}