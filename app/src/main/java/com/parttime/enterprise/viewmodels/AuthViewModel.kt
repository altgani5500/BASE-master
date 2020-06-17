package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.apiclients.ServerConstant
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.AppValidator
import com.parttime.enterprise.uicomman.auth.authmodels.AuthLoginModel
import com.parttime.enterprise.uicomman.auth.devicetokenmodels.DeviceToken
import com.parttime.enterprise.uicomman.homes.homesmodels.HomesModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext


class AuthViewModel : ViewModel(), CoroutineScope {

    var validationErr = MutableLiveData<Int>()
    var signInResponse = MutableLiveData<AuthLoginModel>()
    var deviceTokenResponse = MutableLiveData<DeviceToken>()
    var settingResponse = MutableLiveData<HomesModels>()

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    // validation of user Input
    fun isInputValid(email: String, pass: String, isConnected: Boolean): Boolean {
        if (!email.equals("")) {
            if (AppValidator.isValidEmail(email)) {
                if (!pass.equals("")) {
                    if (AppValidator.isValidPassword(pass)) {
                        if (isConnected) {
                            return true
                        } else {
                            validationErr.postValue(R.string.network_error)
                            return false
                        }
                    } else {
                        validationErr.postValue(R.string.error_invalid_password)
                        return false
                    }
                } else {
                    validationErr.postValue(R.string.empty_password)
                    return false
                }

            } else {
                validationErr.postValue(R.string.error_invalid_email)
                return false
            }
        } else {
            validationErr.postValue(R.string.empty_email)
            return false
        }

        return false

    }


    // api for login
    fun loginUserApi(email: String, pass: String, isConnected: Boolean, lan: String) {

        if (isInputValid(email, pass, isConnected)) {
            val map = HashMap<String, String>()
            map.put(ServerConstant.API_KEY_USERNAME, email)
            map.put(ServerConstant.API_KYE_PASSWORD, pass)
            val apiService = ApiService.init()
            val call: Call<AuthLoginModel> = apiService.userLogin(lan, map)
            call.enqueue(object : Callback<AuthLoginModel> {
                override fun onFailure(call: Call<AuthLoginModel>?, t: Throwable?) {
                    Log.e("ERR", call.toString())
                    validationErr.postValue(R.string.error_generic)
                }

                override fun onResponse(call: Call<AuthLoginModel>?, response: Response<AuthLoginModel>?) {
                    if (response!!.code() == 200) {
                        signInResponse.postValue(response.body()!!)
                    } else {
                        validationErr.postValue(R.string.error_generic)

                    }

                }

            })

        }
    }

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

    // App Role Settings
    fun deviceSetting( isConnected: Boolean, authKey: String, lang: String) {
        //   if (deviceToken.length > 3 && !deviceType.isEmpty()) {
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}