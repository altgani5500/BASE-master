package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.apiclients.ServerConstant
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.AppValidator
import com.parttime.enterprise.uicomman.forgots.forgotModel.ForgotModelResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class ForgotViewModel : ViewModel(), CoroutineScope {

    var validationErr = MutableLiveData<Int>()
    var forgotResponse = MutableLiveData<ForgotModelResponse>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    // user input validation
    fun emailIdValidation(email: String, isConnected: Boolean): Boolean {
        if (!email.equals("")) {
            if (AppValidator.isValidEmail(email)) {
                if (isConnected) {
                    return true
                } else {
                    validationErr.postValue(R.string.network_error)
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

    }

    // forgot api service calling
    fun forgotApiCall(email: String, forgotType: String, isConnected: Boolean, lang: String) {
        if (emailIdValidation(email, isConnected)) {
            val mapForgot = HashMap<String, String>()
            mapForgot.put(ServerConstant.API_KEY_EMAIL, email)
            mapForgot.put(ServerConstant.API_KEY_FORGOT_TYPE, forgotType)
            val apiService = ApiService.init()
            val call: Call<ForgotModelResponse> = apiService.userForgotPass(lang, mapForgot)
            call.enqueue(object : Callback<ForgotModelResponse> {
                override fun onFailure(call: Call<ForgotModelResponse>?, t: Throwable?) {
                    Log.e("ForgotERR", call.toString())
                    validationErr.postValue(R.string.error_generic)
                }

                override fun onResponse(call: Call<ForgotModelResponse>?, response: Response<ForgotModelResponse>?) {
                    if (response!!.code() == 200) {
                        forgotResponse.postValue(response.body()!!)
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