package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.advancesetting.advancesettingmodel.AdvanceSettingResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class AdvanceSettingViewModel : ViewModel(), CoroutineScope {

    var validationErr = MutableLiveData<Int>()
    var advanceSetingViewModel = MutableLiveData<AdvanceSettingResponse>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    fun updateAdvanceSetting(
        authKey: String,
        hourlySetting: String,
        enrollSetting: String,
        lang: String,
        isConnected: Boolean
    ) {
        val map = HashMap<String, String>()
        map.put("hourlyRate", hourlySetting)
        map.put("enrolledWorker", enrollSetting)
        val apiService = ApiService.init()
        val call: Call<AdvanceSettingResponse> = apiService.updateAdvanceSettingAPI("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<AdvanceSettingResponse> {
            override fun onFailure(call: Call<AdvanceSettingResponse>?, t: Throwable?) {
                Log.e("Sub_ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<AdvanceSettingResponse>?, response: Response<AdvanceSettingResponse>?) {
                if (response!!.code() == 200) {
                    advanceSetingViewModel.postValue(response.body()!!)
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