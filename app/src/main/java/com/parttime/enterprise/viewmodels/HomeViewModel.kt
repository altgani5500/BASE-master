package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.homes.homesmodels.HomesModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class HomeViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    var settingResponse = MutableLiveData<HomesModels>()
    var validationErr = MutableLiveData<Int>()


    // App Role Settings
    fun deviceSetting( isConnected: Boolean, authKey: String, lang: String) {
        if (isConnected) {
            val map = HashMap<String, String>()
            map.put(" "," ")
            val apiService = ApiService.init()
            val call: Call<HomesModels> = apiService.userRoleSetting("Bearer $authKey", lang, map)
            call.enqueue(object : Callback<HomesModels> {
                override fun onFailure(call: Call<HomesModels>?, t: Throwable?) {
                    Log.e("ERR", call.toString())
                    validationErr.postValue(R.string.error_generic)
                }

                override fun onResponse(call: Call<HomesModels>?, response: Response<HomesModels>?) {
                    if (response!!.code() == 200) {
                        settingResponse.postValue(response.body()!!)
                    } else {
                        validationErr.postValue(R.string.error_generic)

                    }

                }

            })
        }


    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}