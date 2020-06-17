package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.apiclients.ServerConstant
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.auth.devicetokenmodels.DeviceToken
import com.parttime.enterprise.uicomman.fragments.jobslist.jobsdelete.DeleteJobResponse
import com.parttime.enterprise.uicomman.fragments.jobslist.jobslistmodels.JobListResponseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class JobListFragmentViewModel : ViewModel(), CoroutineScope {

    var validationErr = MutableLiveData<Int>()
    var jobListResponseModel = MutableLiveData<JobListResponseModel>()
    var delModelResponse = MutableLiveData<DeleteJobResponse>()
    var deviceTokenResponse = MutableLiveData<DeviceToken>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    // api for Get jobList
    fun getJobListApi(authKey: String, lang: String, isConnected: Boolean) {
        if(isConnected) {
            val map = HashMap<String, String>()
            map.put("", "")
            val apiService = ApiService.init()
            val call: Call<JobListResponseModel> = apiService.getJobsList("Bearer $authKey", lang, map)
            call.enqueue(object : Callback<JobListResponseModel> {
                override fun onFailure(call: Call<JobListResponseModel>?, t: Throwable?) {
                    Log.e("ERR", t?.printStackTrace().toString())
                    validationErr.postValue(R.string.error_generic)
                }

                override fun onResponse(call: Call<JobListResponseModel>?, response: Response<JobListResponseModel>?) {
                    if (response!!.code() == 200) {
                        jobListResponseModel.postValue(response.body()!!)
                        Log.e("JOBLIST", response.body().toString())
                    } else {
                        validationErr.postValue(R.string.error_generic)
                    }
                }
            })
        }else{
            validationErr.postValue(R.string.network_error)
        }

    }


    // api device token
    fun deviceTokenApi(deviceToken: String, deviceType: String, isConnected: Boolean, authKey: String, lang: String) {
        if(isConnected) {
            if (deviceToken.length > 3 && !deviceType.isEmpty()) {
                val map = HashMap<String, String>()
                map.put(ServerConstant.API_KEY_DEVICE_TOKEN, deviceToken)
                map.put(ServerConstant.API_KEY_DEVICE_TYPE, deviceType)
                val apiService = ApiService.init()
                val call: Call<DeviceToken> = apiService.userDeviceToken("Bearer $authKey", lang, map)
                call.enqueue(object : Callback<DeviceToken> {
                    override fun onFailure(call: Call<DeviceToken>?, t: Throwable?) {
                        Log.e("ERR", call.toString())
                        validationErr.postValue(R.string.error_generic)
                    }

                    override fun onResponse(call: Call<DeviceToken>?, response: Response<DeviceToken>?) {
                        if (response!!.code() == 200) {
                            deviceTokenResponse.postValue(response.body()!!)
                        } else {
                            validationErr.postValue(R.string.error_generic)

                        }

                    }

                })

            } else {
                //  Device token is empty
                validationErr.postValue(R.string.device_token_msg)
                Log.e("AUTH MODEL DEVICE TOKEN", deviceToken + " " + deviceType)
            }

        }else{
            validationErr.postValue(R.string.network_error)
        }
    }

    /*Api For Job Deletes*/
    fun delJobListApi(authKey: String, id: String, isConnected: Boolean, lang: String) {
        val map = HashMap<String, String>()
        map["jobId"] = id
        val apiService = ApiService.init()
        val call: Call<DeleteJobResponse> = apiService.deleteJobApi("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<DeleteJobResponse> {
            override fun onFailure(call: Call<DeleteJobResponse>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<DeleteJobResponse>?, response: Response<DeleteJobResponse>?) {
                if (response!!.code() == 200) {
                    delModelResponse.postValue(response.body()!!)
                    Log.e("Delete Job Res", response.body().toString())
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