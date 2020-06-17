package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.chat.singlechat.dialogmodels.ChatDialogResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class SingleChatViewModels : ViewModel(), CoroutineScope {
    var validationErr = MutableLiveData<Int>()
    var singleChatViewmodel = MutableLiveData<ChatDialogResponse>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    // api for Api for Dialog Chat
    fun setDialogChatApi(
        authKey: String,
        lang: String,
        msg: String,
        startTime: String,
        endTime: String,
        date: String,
        senderId: String,
        receiverId: String
    ) {
        val map = HashMap<String, String>()
        map.put("startTime", startTime)
        map.put("endTime", endTime)
        map.put("date", date)
        map.put("message", msg)
        map.put("senderId", senderId)
        map.put("receiverId", receiverId)
        val apiService = ApiService.init()
        val call: Call<ChatDialogResponse> = apiService.setDialogChat("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<ChatDialogResponse> {
            override fun onFailure(call: Call<ChatDialogResponse>?, t: Throwable?) {
                Log.e("MSG_ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<ChatDialogResponse>?, response: Response<ChatDialogResponse>?) {
                if (response!!.code() == 200) {
                    singleChatViewmodel.postValue(response.body()!!)
                    Log.e("MSG_LIST_RESPONSE", response.body().toString())
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