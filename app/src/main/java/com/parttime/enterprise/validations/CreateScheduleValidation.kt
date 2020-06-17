package com.parttime.enterprise.validations

import android.app.Activity
import android.content.Context
import com.parttime.enterprise.R

class CreateScheduleValidation {


    fun isInputValidJobAddJobs(
        date:String,
        startTime:String,
        endTime:String,
        prevDate:String,
        preStartTime:String,
        prevEndTime:String,
        context:Context

    ): ValidationMessageReturn {
       if(date.trim().length>5){
         if(startTime.trim().length>3){
             if(endTime.trim().length>3){
                 return  ValidationMessageReturn(
                     status = 1,
                     errMessage = " "
                 )
             }else{
                 return  ValidationMessageReturn(
                     status = 0,
                     errMessage = context.getString(R.string.err_schedule_end_time)
                 )
             }
         }else{
             return  ValidationMessageReturn(
                 status = 0,
                 errMessage = context.getString(R.string.err_schedule_start_time)
             )
         }
       }else{
         return  ValidationMessageReturn(
               status = 0,
               errMessage = context.getString(R.string.err_schedue_date_one)
           )
       }


        //  return  ValidationMessageReturn(status = 0,errMessage ="" )

    }

}