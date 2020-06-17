package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.createtask.CreateTaskResponse
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.superevaluatormodel.SuperEvaluatorModelsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class CreateTaskViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    var validationErr = MutableLiveData<Int>()
    var createTaskViewModel = MutableLiveData<SuperEvaluatorModelsResponse>()
    var createTaskViewModel2 = MutableLiveData<CreateTaskResponse>()


    fun getSuperEvaluatorList(
        authKey: String,
        lang: String,
        isConnected: Boolean
    ) {
        val map = HashMap<String, String>()
        map.put(" ", " ")
        val apiService = ApiService.init()
        val call: Call<SuperEvaluatorModelsResponse> =
            apiService.getSuperEvaluatorList("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<SuperEvaluatorModelsResponse> {
            override fun onFailure(call: Call<SuperEvaluatorModelsResponse>?, t: Throwable?) {
                Log.e("WORKER_PROFILE_LIST", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<SuperEvaluatorModelsResponse>?,
                response: Response<SuperEvaluatorModelsResponse>?
            ) {
                if (response!!.code() == 200) {
                    createTaskViewModel.postValue(response.body()!!)
                    Log.e("WORKER_PROFILE_LIST_RES", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)
                }

            }

        })


    }

    fun createTaskThroughApi(
        authKey: String,
        lang: String,
        map : HashMap<String, String>,
        isConnected: Boolean
    ) {

        val apiService = ApiService.init()
        val call: Call<CreateTaskResponse> =
            apiService.createTask("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<CreateTaskResponse> {
            override fun onFailure(call: Call<CreateTaskResponse>?, t: Throwable?) {
                Log.e("WORKER_PROFILE_LIST", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<CreateTaskResponse>?,
                response: Response<CreateTaskResponse>?
            ) {
                if (response!!.code() == 200) {
                    createTaskViewModel2.postValue(response.body()!!)
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