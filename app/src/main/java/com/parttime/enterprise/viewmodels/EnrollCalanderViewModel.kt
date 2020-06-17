package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.enrollcalander.schedulecalander.ScheduleCalenderResponseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class EnrollCalanderViewModel : ViewModel(), CoroutineScope {

    var validationErr = MutableLiveData<Int>()
    var enrollCalanderViewModel = MutableLiveData<ScheduleCalenderResponseModel>()
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    fun getSchedulesCalanders(
        authKey: String,
        lang: String,
        isConnected: Boolean,
        month:String,
        year:String
    ) {
        val map = HashMap<String, String>()
        map.put("month", month)
        map.put("year",year)
        val apiService = ApiService.init()
        val call: Call<ScheduleCalenderResponseModel> = apiService.getSchedulesCalander("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<ScheduleCalenderResponseModel> {
            override fun onFailure(call: Call<ScheduleCalenderResponseModel>?, t: Throwable?) {
                Log.e("Sub_ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<ScheduleCalenderResponseModel>?, response: Response<ScheduleCalenderResponseModel>?) {
                if (response!!.code() == 200) {
                    enrollCalanderViewModel.postValue(response.body()!!)
                    Log.e("Advance_Setting", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)
                }

            }

        })


    }

    fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
        observeForever(object: Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }

    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
        observe(owner, object: Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}