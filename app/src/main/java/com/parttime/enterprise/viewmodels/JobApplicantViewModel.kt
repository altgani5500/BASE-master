package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.fragments.applicant.acceptfinal.AcceptInterviewResponse
import com.parttime.enterprise.uicomman.fragments.applicant.applicantmodel.ApplicantModelResponse
import com.parttime.enterprise.uicomman.fragments.applicant.enrollaccept.EnrollAcceptModel
import com.parttime.enterprise.uicomman.fragments.jobslist.jobsdelete.DeleteJobResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class JobApplicantViewModel : ViewModel(), CoroutineScope {

    var applicangtResponseModel = MutableLiveData<ApplicantModelResponse>()
    var applicantDelResponseModel = MutableLiveData<DeleteJobResponse>()
    var enrollAcceptModell = MutableLiveData<EnrollAcceptModel>()
    var acceptFinalInterview = MutableLiveData<AcceptInterviewResponse>()
    var validationErr = MutableLiveData<Int>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    /*Api for enroll user accept*/
    fun acceptInterView(authKey: String, id: String, lang: String, msg: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map["jobApplyId"] = id
        map["message"] = msg
        val apiService = ApiService.init()
        val call: Call<AcceptInterviewResponse> = apiService.acceptForInterviewApi("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<AcceptInterviewResponse> {
            override fun onFailure(call: Call<AcceptInterviewResponse>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(
                call: Call<AcceptInterviewResponse>?,
                response: Response<AcceptInterviewResponse>?
            ) {
                if (response!!.code() == 200) {
                    acceptFinalInterview.postValue(response.body()!!)
                    Log.e("Delete Job Res", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)

                }

            }

        })
    }


    /*Api for enroll user accept*/
    fun enrollAcceptApiSet(authKey: String, id: String, lang: String, msg: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map["jobApplyId"] = id
        map["message"] = msg
        val apiService = ApiService.init()
        val call: Call<EnrollAcceptModel> = apiService.acceptForUserEnrollApi("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<EnrollAcceptModel> {
            override fun onFailure(call: Call<EnrollAcceptModel>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<EnrollAcceptModel>?, response: Response<EnrollAcceptModel>?) {
                if (response!!.code() == 200) {
                    enrollAcceptModell.postValue(response.body()!!)
                    Log.e("Delete Job Res", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)

                }

            }

        })
    }


    /*Api For Applicant Deletes*/
    fun delApplibListApi(authKey: String, id: String, isConnected: Boolean) {
        val map = HashMap<String, String>()
        map["jobApplyId"] = id
        val apiService = ApiService.init()
        val call: Call<DeleteJobResponse> = apiService.deleteApplicantApi("Bearer $authKey", map)
        call.enqueue(object : Callback<DeleteJobResponse> {
            override fun onFailure(call: Call<DeleteJobResponse>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<DeleteJobResponse>?, response: Response<DeleteJobResponse>?) {
                if (response!!.code() == 200) {
                    applicantDelResponseModel.postValue(response.body()!!)
                    Log.e("Delete Job Res", response.body().toString())
                } else {
                    validationErr.postValue(R.string.error_generic)

                }

            }

        })


    }


    /*Api For Job Applicant*/
    fun applicantListApi(
        authKey: String,
        id: String,
        isConnected: Boolean,
        lang: String,
        hashMap: HashMap<String, String>
    ) {
        val map = HashMap<String, String>()
        map["jobId"] = id
        if (!hashMap.isNullOrEmpty()) {
            map.putAll(hashMap)
        }
        val apiService = ApiService.init()
        val call: Call<ApplicantModelResponse> = apiService.getApplicantsList("Bearer $authKey", lang, map)
        call.enqueue(object : Callback<ApplicantModelResponse> {
            override fun onFailure(call: Call<ApplicantModelResponse>?, t: Throwable?) {
                Log.e("Applicanyt err", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<ApplicantModelResponse>?, response: Response<ApplicantModelResponse>?) {
                if (response!!.code() == 200) {
                    applicangtResponseModel.postValue(response.body()!!)
                    Log.e("Applicant Job Response", response.body().toString())
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