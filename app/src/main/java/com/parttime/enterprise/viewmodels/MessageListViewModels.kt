package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.fragments.messages.messagemodel.MessageListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MessageListViewModels : ViewModel(), CoroutineScope {
    var validationErr = MutableLiveData<Int>()
    var msgResponseModel = MutableLiveData<MessageListResponse>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    // api for Get MessageList
    fun getMessagesApi(authKey: String, lang: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map.put("", "")
        val apiService = ApiService.init()
        val call: Call<MessageListResponse> = apiService.getMsgList("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<MessageListResponse> {
            override fun onFailure(call: Call<MessageListResponse>?, t: Throwable?) {
                Log.e("MSG_ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<MessageListResponse>?, response: Response<MessageListResponse>?) {
                if (response!!.code() == 200) {
                    msgResponseModel.postValue(response.body()!!)
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