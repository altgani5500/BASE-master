package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.locationaddress.statemodel.StateModelResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class StateListViewModel : ViewModel(), CoroutineScope {
    var validationErr = MutableLiveData<Int>()
    var stateResponse = MutableLiveData<StateModelResponse>()

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    // api for Get state List
    fun getStateList(authKey: String, lang: String, selectedCountry: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map.put("country", selectedCountry)
        val apiService = ApiService.init()
        val call: Call<StateModelResponse> = apiService.getState("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<StateModelResponse> {
            override fun onFailure(call: Call<StateModelResponse>?, t: Throwable?) {
                Log.e("STATEERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<StateModelResponse>?, response: Response<StateModelResponse>) {
                if (response.code() == 200) {
                    stateResponse.postValue(response.body())
                    Log.e("STATELIST", response.body().toString())
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