package com.parttime.enterprise.validations

import android.app.Activity
import com.parttime.enterprise.R

class EditJobInputValidation {


    fun isInputValidJobEditJobs(
        hourlyRangeTos:String,
        context: Activity,
        uiDesigner: String,
        jobDesc: String,
        location: String,
        branchName: String,
        currancy: String,
        hourlyRateSpn: String,
        hourlyRateEdt: String,
        totalHourweek: String,
        applicant: String,
        hoursOfExp: String,
        userName: String,
        pass: String,
        requirement: String,
        workGuideline: String,
        isConnected: Boolean
    ): ValidationMessageReturn {
        if (uiDesigner.isNotEmpty()) {
            if (jobDesc.isNotEmpty()) {
                if (location.isNotEmpty()) {
                    if (branchName.isNotEmpty()) {
                        if (currancy.isNotEmpty()) {
                            if (hourlyRateSpn.isNotEmpty()) {
                                //   if (hourlyRateEdt.isNotEmpty()) {
                              //  if (totalHourweek.isNotEmpty()) {
                                    if (applicant.isNotEmpty()) {
                                        if (hoursOfExp.isNotEmpty() && hoursOfExp.trim().toInt()<1153 && hoursOfExp.trim().toInt()>hourlyRangeTos.trim().toInt()) {
                                            if (userName.isNotEmpty()) {
                                                if (pass.trim().isNotEmpty()) {
                                                    return if (requirement.isNotEmpty()) {
                                                        if(hourlyRangeTos.isNotEmpty()&& hourlyRangeTos.trim().toInt()<=1000 && hourlyRangeTos.trim().toInt()!=hoursOfExp.trim().toInt()
                                                            && hourlyRangeTos.trim().toInt()<hoursOfExp.trim().toInt()){
                                                            if (workGuideline.isNotEmpty()) {
                                                                if (isConnected) {
                                                                    ValidationMessageReturn(status = 1, errMessage = "")
                                                                } else {
                                                                    ValidationMessageReturn(
                                                                        status = 0,
                                                                        errMessage = context.getString(R.string.network_error)
                                                                    )
                                                                }
                                                            } else {
                                                                ValidationMessageReturn(
                                                                    status = 0,
                                                                    errMessage = context.getString(R.string.err_work_guidelines)
                                                                )
                                                            }
                                                        }else{
                                                            ValidationMessageReturn(
                                                                status = 0,
                                                                errMessage = context.getString(R.string.tos)
                                                            )
                                                        }


                                                    } else {
                                                        ValidationMessageReturn(
                                                            status = 0,
                                                            errMessage = context.getString(R.string.err_requirements)
                                                        )
                                                    }
                                                } else {
                                                    return ValidationMessageReturn(
                                                        status = 0,
                                                        errMessage = context.getString(R.string.err_wifyUserpass)
                                                    )
                                                }
                                            } else {
                                                return ValidationMessageReturn(
                                                    status = 0,
                                                    errMessage = context.getString(R.string.err_wifyUserName)
                                                )
                                            }
                                        } else {
                                            return ValidationMessageReturn(
                                                status = 0,
                                                errMessage = context.getString(R.string.fromss)
                                            )
                                        }
                                    } else {
                                        return ValidationMessageReturn(
                                            status = 0,
                                            errMessage = context.getString(R.string.maximul_applicants)
                                        )
                                    }
                               // } else {
                              //      return ValidationMessageReturn(
                               //         status = 0,
                                //        errMessage = context.getString(R.string.select_hour_of_week)
                                 //   )
                               // }
                                //} else {
                                //    return  ValidationMessageReturn(status = 0,errMessage =context.getString(R.string.edt_hourly_rate) )
                                // }
                            } else {
                                return ValidationMessageReturn(
                                    status = 0,
                                    errMessage = context.getString(R.string.please_select_hourly_rate)
                                )
                            }
                        } else {
                            return ValidationMessageReturn(
                                status = 0,
                                errMessage = context.getString(R.string.err_jobcurrency)
                            )
                        }
                    } else {
                        return ValidationMessageReturn(
                            status = 0,
                            errMessage = context.getString(R.string.please_enter_branch)
                        )
                    }
                } else {
                    return ValidationMessageReturn(status = 0, errMessage = context.getString(R.string.err_jobLoc))
                }
            } else {
                return ValidationMessageReturn(status = 0, errMessage = context.getString(R.string.err_jobdesc))

            }
        } else {
            return ValidationMessageReturn(status = 0, errMessage = context.getString(R.string.err_Jobtitle))
        }
    }


}