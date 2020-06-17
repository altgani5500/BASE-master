package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels.ScheduleTimeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class CalenderTaskListViewModel : ViewModel(), CoroutineScope {

    var validationErr = MutableLiveData<Int>()
    var scheduleListViewModel = MutableLiveData<ScheduleTimeResponse>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    fun getTaskThroughApi(
        authKey: String,
        dates: String,
        lang: String,
        isConnected: Boolean
    ) {
        val map = HashMap<String, String>()
        map.put("date", dates)
        val apiService = ApiService.init()
        val call: Call<ScheduleTimeResponse> = apiService.getScheduleList("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<ScheduleTimeResponse> {
            override fun onFailure(call: Call<ScheduleTimeResponse>?, t: Throwable?) {
                Log.e("Sub_ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<ScheduleTimeResponse>?, response: Response<ScheduleTimeResponse>?) {
                if (response!!.code() == 200) {
                    scheduleListViewModel.postValue(response.body()!!)
                    Log.e("Advance_Setting", response.body().toString())
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