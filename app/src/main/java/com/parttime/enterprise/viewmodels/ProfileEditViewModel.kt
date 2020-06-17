package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.profileedit.indusrtymodel.IndustryModel
import com.parttime.enterprise.uicomman.profileedit.indusrtymodel.UpdateProfileResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.CoroutineContext

class ProfileEditViewModel : ViewModel(), CoroutineScope {
    var validationErr = MutableLiveData<Int>()
    var industryModelResponse = MutableLiveData<IndustryModel>()
    var profileUpdate = MutableLiveData<UpdateProfileResponse>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun getPartTimeList(lang: String) {
        val apiService = ApiService.initGet()
        val call: Call<IndustryModel> = apiService.getIndustryApi(lang)

        call.enqueue(object : Callback<IndustryModel> {
            override fun onFailure(call: Call<IndustryModel>?, t: Throwable?) {
                Log.e("INDUSTRY ERR", call.toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<IndustryModel>?, response: Response<IndustryModel>?) {
                if (response!!.code() == 200) {
                    industryModelResponse.postValue(response.body()!!)
                } else {
                    validationErr.postValue(R.string.error_generic)

                }

            }

        })
    }


    fun editProfileApi(
        userName: String,
        catId: String,
        gen: String,
        file: File?,
        token: String,
        isConnected: Boolean,
        lang: String
    ) {

        //  if(isInputValid(full_name, isConnected)){
        val map = HashMap<String, RequestBody>()
        map.put("name", getRequestBody(userName))
        map.put("categoryId", getRequestBody(catId))
        map.put("generalInfo", getRequestBody(gen))
        var bodyProfilePic: MultipartBody.Part? = null
        if (file != null) {
            val image = RequestBody.create(MediaType.parse("image/*"), file)
            bodyProfilePic = MultipartBody.Part.createFormData("profilePicture", file.name, image)
        }
        val apiService = ApiService.init()
        val call: Call<UpdateProfileResponse> = apiService.editProfile("Bearer $token", lang, map, bodyProfilePic)
        call.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onFailure(call: Call<UpdateProfileResponse>?, t: Throwable?) {
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<UpdateProfileResponse>?, response: Response<UpdateProfileResponse>?) {
                if (response!!.code() == 200) {
                    profileUpdate.postValue(response.body()!!)
                } else {
                    validationErr.postValue(R.string.error_generic)

                }

            }

        })

        // }
    }

    private fun getRequestBody(value: String?): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}