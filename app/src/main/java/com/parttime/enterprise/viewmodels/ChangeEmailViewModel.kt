package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.accountsettings.accountsetting.AccountSettingResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class ChangeEmailViewModel : ViewModel(), CoroutineScope {


    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    var validationErr = MutableLiveData<Int>()
    var accountSettingResponse = MutableLiveData<AccountSettingResponse>()


    // api for login
    fun changeAccountSettingEnterPrise(authKey: String, passOld: String, isConnected: Boolean, lan: String) {

        val map = HashMap<String, String>()
        map.put("key", "email")
        map.put("value", passOld)
        val apiService = ApiService.init()
        val call: Call<AccountSettingResponse> = apiService.accountSetting("Bearer $authKey", lan, map)
        call.enqueue(object : Callback<AccountSettingResponse> {
            override fun onFailure(call: Call<AccountSettingResponse>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<AccountSettingResponse>?, response: Response<AccountSettingResponse>?) {
                if (response!!.code() == 200) {
                    accountSettingResponse.postValue(response.body()!!)
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