package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.applicantfilter.educationdetails.EducationDetails
import com.parttime.enterprise.uicomman.applicantfilter.educationlevel.EducationLevel
import com.parttime.enterprise.uicomman.applicantfilter.nationality.NationalityResponse
import com.parttime.enterprise.uicomman.appsearch.searchresults.SearchResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class AppSearchViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    var validationErr = MutableLiveData<Int>()
    var educationLevelResponse = MutableLiveData<EducationLevel>()
    var nationalLisResponse = MutableLiveData<NationalityResponse>()
    var educationDetailsListResponse = MutableLiveData<EducationDetails>()
    var searchResultResponse = MutableLiveData<SearchResponse>()


    fun appSearchResponse(authKey: String, map: HashMap<String, String>, isConnected: Boolean, lan: String) {

        val apiService = ApiService.init()
        Log.e("APP_SEARCH_request", map.toString())
        val call: Call<SearchResponse> = apiService.applicantJobSearchApi("Bearer $authKey", lan, map)
        call.enqueue(object : Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>?, t: Throwable?) {
                Log.e("JOB_SEARCH_ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<SearchResponse>?, response: Response<SearchResponse>?) {
                if (response!!.code() == 200) {
                    searchResultResponse.postValue(response.body()!!)
                } else {
                    validationErr.postValue(R.string.error_generic)

                }

            }

        })


    }


    fun getEducationList(lang: String) {
        val apiService = ApiService.initGet()
        val call: Call<EducationLevel> = apiService.getEducationLevelList(lang)

        call.enqueue(object : Callback<EducationLevel> {
            override fun onFailure(call: Call<EducationLevel>?, t: Throwable?) {
                Log.e("Education Level ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<EducationLevel>?, response: Response<EducationLevel>?) {
                if (response!!.code() == 200) {
                    educationLevelResponse.postValue(response.body()!!)
                } else {
                    validationErr.postValue(R.string.error_generic)

                }

            }

        })
    }


    fun getNationality(lang: String) {
        val apiService = ApiService.initGet()
        val call: Call<NationalityResponse> = apiService.getNationalList(lang)

        call.enqueue(object : Callback<NationalityResponse> {
            override fun onFailure(call: Call<NationalityResponse>?, t: Throwable?) {
                Log.e("Education Level ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<NationalityResponse>?, response: Response<NationalityResponse>?) {
                if (response!!.code() == 200) {
                    nationalLisResponse.postValue(response.body()!!)
                } else {
                    validationErr.postValue(R.string.error_generic)

                }

            }

        })
    }


    fun getEducationDetailList(lang: String) {
        val apiService = ApiService.initGet()
        val call: Call<EducationDetails> = apiService.getEducationDetailsList(lang)

        call.enqueue(object : Callback<EducationDetails> {
            override fun onFailure(call: Call<EducationDetails>?, t: Throwable?) {
                Log.e("Education Level ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<EducationDetails>?, response: Response<EducationDetails>?) {
                if (response!!.code() == 200) {
                    educationDetailsListResponse.postValue(response.body()!!)
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