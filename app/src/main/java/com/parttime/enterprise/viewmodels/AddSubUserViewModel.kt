package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.subuser.adduserlist.GetSubUserListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class AddSubUserViewModel : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    var validationErr = MutableLiveData<Int>()
    var subUserGetViewModelRes = MutableLiveData<GetSubUserListResponse>()

    fun getSubUserList(authKey: String, lang: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map.put("", "")
        val apiService = ApiService.init()
        val call: Call<GetSubUserListResponse> = apiService.getSubUserListApi("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<GetSubUserListResponse> {
            override fun onFailure(call: Call<GetSubUserListResponse>?, t: Throwable?) {
                Log.e("Sub_ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<GetSubUserListResponse>?, response: Response<GetSubUserListResponse>?) {
                if (response!!.code() == 200) {
                    subUserGetViewModelRes.postValue(response.body()!!)
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