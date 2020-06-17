package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.locationaddress.countrymodel.CountryListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class CountryListViewModel : ViewModel(), CoroutineScope {
    var validationErr = MutableLiveData<Int>()
    var countryResponse = MutableLiveData<CountryListResponse>()

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    // api for Get Country List
    fun getCountryList(authKey: String, lang: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map.put("", "")
        val apiService = ApiService.init()
        val call: Call<CountryListResponse> = apiService.getCountry("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<CountryListResponse> {
            override fun onFailure(call: Call<CountryListResponse>?, t: Throwable?) {
                Log.e("COUNTRYERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<CountryListResponse>?, response: Response<CountryListResponse>?) {
                if (response!!.code() == 200) {
                    countryResponse.postValue(response.body()!!)
                    Log.e("COUNTRYLIST", response.body().toString())
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