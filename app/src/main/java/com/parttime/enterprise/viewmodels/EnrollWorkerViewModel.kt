package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.fragments.enroll.workesmodels.response.EnrollWorkerListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class EnrollWorkerViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    var validationErr = MutableLiveData<Int>()
    var enrollWorkerListViewModel = MutableLiveData<EnrollWorkerListResponse>()


    // api for Get jobList
    fun getEnrollWorkerListApi(authKey: String, enrollSearch: String, lang: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map[""] = ""
        if (!enrollSearch.trim().isEmpty()) {
            map["searchName"] = enrollSearch
        }
        val apiService = ApiService.init()
        val call: Call<EnrollWorkerListResponse> = apiService.getEnrollWorkerList("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<EnrollWorkerListResponse> {
            override fun onFailure(call: Call<EnrollWorkerListResponse>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<EnrollWorkerListResponse>?,
                response: Response<EnrollWorkerListResponse>?
            ) {
                if (response!!.code() == 200) {
                    enrollWorkerListViewModel.postValue(response.body()!!)
                    Log.e("JOBLIST", response.body().toString())
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