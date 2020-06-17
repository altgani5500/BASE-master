package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.ApplicantProfileModelResponse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class ApplicantProfileViewModel : ViewModel(), CoroutineScope {

    var validationErr = MutableLiveData<Int>()
    var applicantResponseModel = MutableLiveData<ApplicantProfileModelResponse>()

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    /*Applicant Profile Data*/
    fun getApplicantProfilApi(authKey: String, applcntId: String, lang: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map.put("applicantId", applcntId)
        val apiService = ApiService.init()
        val call: Call<ApplicantProfileModelResponse> = apiService.getApplicantProfileData("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<ApplicantProfileModelResponse> {
            override fun onFailure(call: Call<ApplicantProfileModelResponse>?, t: Throwable?) {
                Log.e("Applicant Profile ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<ApplicantProfileModelResponse>?,
                response: Response<ApplicantProfileModelResponse>?
            ) {
                if (response!!.code() == 200) {
                    applicantResponseModel.postValue(response.body()!!)
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