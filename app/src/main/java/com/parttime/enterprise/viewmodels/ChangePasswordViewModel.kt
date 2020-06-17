package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.accountsettings.changepass.ChangePasswordModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class ChangePasswordViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    var validationErr = MutableLiveData<Int>()
    var changePasswordModel = MutableLiveData<ChangePasswordModel>()


    // api for login
    fun changeAccountSettingPassword(
        authKey: String,
        passOld: String,
        passNew: String,
        isConnected: Boolean,
        lan: String
    ) {

        val map = HashMap<String, String>()
        map.put("oldPassword", passOld)
        map.put("newPassword", passNew)
        val apiService = ApiService.init()
        val call: Call<ChangePasswordModel> = apiService.accountSettingPassword("Bearer $authKey", lan, map)
        call.enqueue(object : Callback<ChangePasswordModel> {
            override fun onFailure(call: Call<ChangePasswordModel>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<ChangePasswordModel>?, response: Response<ChangePasswordModel>?) {
                if (response!!.code() == 200) {
                    changePasswordModel.postValue(response.body()!!)
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