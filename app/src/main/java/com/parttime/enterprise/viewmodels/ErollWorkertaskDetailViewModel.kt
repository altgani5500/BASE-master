package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.detail.TaskDetailResponseModel
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.dialogevolution.DialogEvolutionModelRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class ErollWorkertaskDetailViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    var validationErr = MutableLiveData<Int>()
    var workerProfileDetails = MutableLiveData<TaskDetailResponseModel>()

    var workerProfileDetailsDilog = MutableLiveData<DialogEvolutionModelRes>()



    fun taskProfileDetails(
        authKey: String,
        taskid: String,
        lang: String,
        isConnected: Boolean
    ) {
        val map = HashMap<String, String>()
        map.put("taskId", taskid)
        val apiService = ApiService.init()
        val call: Call<TaskDetailResponseModel> = apiService.taskProfileDetails("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<TaskDetailResponseModel> {
            override fun onFailure(call: Call<TaskDetailResponseModel>?, t: Throwable?) {
                Log.e("WORKER_PROFILE_LIST", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<TaskDetailResponseModel>?, response: Response<TaskDetailResponseModel>?) {
                if (response!!.code() == 200) {
                    workerProfileDetails.postValue(response.body()!!)
                    Log.e("WORKER_PROFILE_LIST_RES", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)
                }

            }

        })


    }

    fun taskProfileDetailsDialogEvolution(
        authKey: String,
        map : HashMap<String, String>,
        lang: String,
        isConnected: Boolean
    ) {

        val apiService = ApiService.init()
        val call: Call<DialogEvolutionModelRes> = apiService.taskEvolutionDialog("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<DialogEvolutionModelRes> {
            override fun onFailure(call: Call<DialogEvolutionModelRes>?, t: Throwable?) {
                Log.e("WORKER_DIALOG", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<DialogEvolutionModelRes>?, response: Response<DialogEvolutionModelRes>?) {
                if (response!!.code() == 200) {
                    workerProfileDetailsDilog.postValue(response.body()!!)
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