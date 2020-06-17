package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.apiclients.ServerConstant
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.auth.devicetokenmodels.DeviceToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class NotificationSettingViewModels : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    var deviceTokenResponse = MutableLiveData<DeviceToken>()
    var validationErr = MutableLiveData<Int>()


    // api device token
    fun deviceTokenApi(deviceToken: String, deviceType: String, isConnected: Boolean, authKey: String, lang: String) {
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
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}