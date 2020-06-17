package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.fragments.jobsDetails.models.JobDetailsModelResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class JobDetailsFragmentViewModel : ViewModel(), CoroutineScope {


    var jobDetailsResponseModel = MutableLiveData<JobDetailsModelResponse>()
    var validationErr = MutableLiveData<Int>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    /*Api For Job Details*/
    fun detailsJobListApi(authKey: String, id: String, isConnected: Boolean, lang: String) {
        val map = HashMap<String, String>()
        map["jobId"] = id
        val apiService = ApiService.init()
        val call: Call<JobDetailsModelResponse> = apiService.detailsJobApi("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<JobDetailsModelResponse> {
            override fun onFailure(call: Call<JobDetailsModelResponse>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<JobDetailsModelResponse>?,
                response: Response<JobDetailsModelResponse>?
            ) {
                if (response!!.code() == 200) {
                    jobDetailsResponseModel.postValue(response.body()!!)
                    Log.e("Details Job Response", response.body().toString())
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