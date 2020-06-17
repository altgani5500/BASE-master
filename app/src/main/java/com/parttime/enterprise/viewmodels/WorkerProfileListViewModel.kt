package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist.WorkerProfileListModelResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class WorkerProfileListViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    var validationErr = MutableLiveData<Int>()
    var workerProfileListViewModel = MutableLiveData<WorkerProfileListModelResponse>()



    fun getWorkerProfileList(
        authKey: String,
        profileId: String,
        lang: String,
        isConnected: Boolean
    ) {
        val map = HashMap<String, String>()
        map.put("enrolledUserId", profileId)
        val apiService = ApiService.init()
        val call: Call<WorkerProfileListModelResponse> = apiService.workerProfileList("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<WorkerProfileListModelResponse> {
            override fun onFailure(call: Call<WorkerProfileListModelResponse>?, t: Throwable?) {
                Log.e("WORKER_PROFILE_LIST", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<WorkerProfileListModelResponse>?, response: Response<WorkerProfileListModelResponse>?) {
                if (response!!.code() == 200) {
                    workerProfileListViewModel.postValue(response.body()!!)
                    Log.e("WORKER_PROFILE_LIST_RES", response.body().toString())
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