package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.locationaddress.citymodel.CityModelResponseX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class CityListViewModel : ViewModel(), CoroutineScope {
    var validationErr = MutableLiveData<Int>()
    var cityResponse = MutableLiveData<CityModelResponseX>()

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    // api for Get city List
    fun getcityList(authKey: String, lang: String, selectedState: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map.put("state", selectedState)
        val apiService = ApiService.init()
        val call: Call<CityModelResponseX> = apiService.getCity("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<CityModelResponseX> {
            override fun onFailure(call: Call<CityModelResponseX>?, t: Throwable?) {
                Log.e("CITYERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<CityModelResponseX>?, response: Response<CityModelResponseX>) {
                if (response.code() == 200) {
                    cityResponse.postValue(response.body())
                    Log.e("CITYLIST", response.body().toString())
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