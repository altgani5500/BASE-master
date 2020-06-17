package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.AcceptRejectWorkResponse
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.MonthDetailsModelResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MonthDetailsViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    var validationErr = MutableLiveData<Int>()
    var userWorkReportHistory = MutableLiveData<MonthDetailsModelResponse>()

    var acceptRejectViewModel = MutableLiveData<AcceptRejectWorkResponse>()


    fun getWorkReportHistory(
        authKey: String,
        lang: String,
        userId: String,
        month: String,
        year: String,
        isConnected: Boolean
    ) {
        val map = HashMap<String, String>()
        map.put("userId", userId)
        map.put("month", month)
        map.put("year", year)
        val apiService = ApiService.init()
        val call: Call<MonthDetailsModelResponse> =
            apiService.getMonthDeatilsApi("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<MonthDetailsModelResponse> {
            override fun onFailure(call: Call<MonthDetailsModelResponse>?, t: Throwable?) {
                Log.e("USER_WORK_REPORT", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<MonthDetailsModelResponse>?,
                response: Response<MonthDetailsModelResponse>?
            ) {
                if (response!!.code() == 200) {
                    userWorkReportHistory.postValue(response.body()!!)
                    Log.e("USER_WORK_REPORT", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)
                }

            }

        })


    }

    fun hitForAccepOrReject(
        authKey: String,
        map: HashMap<String, String>,
        lang: String,
        isConnected: Boolean
    ) {

        val apiService = ApiService.init()
        val call: Call<AcceptRejectWorkResponse> =
            apiService.acceptRejectTiming("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<AcceptRejectWorkResponse> {
            override fun onFailure(call: Call<AcceptRejectWorkResponse>?, t: Throwable?) {
                Log.e("ACCEPT", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<AcceptRejectWorkResponse>?,
                response: Response<AcceptRejectWorkResponse>?
            ) {
                if (response!!.code() == 200) {
                    acceptRejectViewModel.postValue(response.body()!!)
                    Log.e("ACCEPT", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)
                }

            }

        })


    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}