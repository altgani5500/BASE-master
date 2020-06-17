package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.schedules.CreateScheduleResponseModel
import com.parttime.enterprise.uicomman.schedules.allenrolllist.GetAllEnrollUserListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class AddScheduleViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    var validationErr = MutableLiveData<Int>()
    var userEnrollListViewModels = MutableLiveData<GetAllEnrollUserListResponse>()

    var createScheduleViewModel = MutableLiveData<CreateScheduleResponseModel>()



    fun getUserEnrollListApi(
        authKey: String,
        lang: String,
        isConnected: Boolean
    ) {
        val map = HashMap<String, String>()
        map.put(" ", " ")
        val apiService = ApiService.init()
        val call: Call<GetAllEnrollUserListResponse> = apiService.getAppUserEnrolList("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<GetAllEnrollUserListResponse> {
            override fun onFailure(call: Call<GetAllEnrollUserListResponse>?, t: Throwable?) {
                Log.e("USER_ENROLL_LIST", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<GetAllEnrollUserListResponse>?, response: Response<GetAllEnrollUserListResponse>?) {
                if (response!!.code() == 200) {
                    userEnrollListViewModels.postValue(response.body()!!)
                    Log.e("USER_ENROLL_LIST", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)
                }

            }

        })


    }

    fun editSchedule(
        authKey: String,
        map : HashMap<String, String>,
        lang: String,
        isConnected: Boolean
    ) {

        val apiService = ApiService.init()
        val call: Call<CreateScheduleResponseModel> = apiService.editsScheduls("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<CreateScheduleResponseModel> {
            override fun onFailure(call: Call<CreateScheduleResponseModel>?, t: Throwable?) {
                Log.e("WORKER_DIALOG", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<CreateScheduleResponseModel>?, response: Response<CreateScheduleResponseModel>?) {
                if (response!!.code() == 200) {
                    createScheduleViewModel.postValue(response.body()!!)
                    Log.e("WORKER_DIALOG", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)
                }

            }

        })


    }



    fun createSchedule(
        authKey: String,
        map : HashMap<String, String>,
        lang: String,
        isConnected: Boolean
    ) {

        val apiService = ApiService.init()
        val call: Call<CreateScheduleResponseModel> = apiService.createScheduls("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<CreateScheduleResponseModel> {
            override fun onFailure(call: Call<CreateScheduleResponseModel>?, t: Throwable?) {
                Log.e("WORKER_DIALOG", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<CreateScheduleResponseModel>?, response: Response<CreateScheduleResponseModel>?) {
                if (response!!.code() == 200) {
                    createScheduleViewModel.postValue(response.body()!!)
                    Log.e("WORKER_DIALOG", response.body().toString())
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