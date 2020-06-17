package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.notifications.notificationmodels.NotificationResponseModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class NotificationListViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    var validationErr = MutableLiveData<Int>()
    var notificationListViewModel = MutableLiveData<NotificationResponseModels>()


    // api for Get notificationList
    fun getNotificationList(authKey: String, lang: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map[""] = ""

        val apiService = ApiService.init()
        val call: Call<NotificationResponseModels> = apiService.getNotificationList("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<NotificationResponseModels> {
            override fun onFailure(call: Call<NotificationResponseModels>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<NotificationResponseModels>?,
                response: Response<NotificationResponseModels>?
            ) {
                if (response!!.code() == 200) {
                    notificationListViewModel.postValue(response.body()!!)
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