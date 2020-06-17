package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.fragments.profileview.profilemodel.ProfileViewModelResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class ProfileGetViewModel : ViewModel(), CoroutineScope {
    var validationErr = MutableLiveData<Int>()

    var profileGetResponse = MutableLiveData<ProfileViewModelResponse>()

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    // api for Get jobList
    fun getProfileData(authKey: String, lang: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map.put("", "")
        val apiService = ApiService.init()
        val call: Call<ProfileViewModelResponse> = apiService.getProfileData("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<ProfileViewModelResponse> {
            override fun onFailure(call: Call<ProfileViewModelResponse>?, t: Throwable?) {
                Log.e("Get Frofile ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<ProfileViewModelResponse>?,
                response: Response<ProfileViewModelResponse>?
            ) {
                if (response!!.code() == 200) {
                    profileGetResponse.postValue(response.body()!!)
                    Log.e("GET PROFILE DATA", response.body().toString())
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