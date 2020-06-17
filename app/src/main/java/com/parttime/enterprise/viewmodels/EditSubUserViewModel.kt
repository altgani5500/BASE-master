package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.subuser.createuser.CreateUserResponse
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

class EditSubUserViewModel : ViewModel(), CoroutineScope {

    var validationErr = MutableLiveData<Int>()

    var createUserResponseViewModels = MutableLiveData<CreateUserResponse>()
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun editSubUser(
        subI: String,
        userName: String,
        email: String,
        pass: String,
        roleid: String,
        file: File?,
        token: String,
        isConnected: Boolean,
        lang: String
    ) {

        //  if(isInputValid(full_name, isConnected)){
        val map = HashMap<String, RequestBody>()
        map["name"] = getRequestBody(userName)
        map["email"] = getRequestBody(email)
        map["password"] = getRequestBody(pass)
        map["roleId"] = getRequestBody(roleid)
        map["subUserId"] = getRequestBody(subI)
        var bodyProfilePic: MultipartBody.Part? = null
        if (file != null) {
            val image = RequestBody.create(MediaType.parse("image/*"), file)
            bodyProfilePic = MultipartBody.Part.createFormData("profilePicture", file.name, image)
        }
        val apiService = ApiService.init()
        val call: Call<CreateUserResponse> = apiService.editSubUserApi("Bearer $token", lang, map, bodyProfilePic)
        call.enqueue(object : Callback<CreateUserResponse> {
            override fun onFailure(call: Call<CreateUserResponse>?, t: Throwable?) {
                Log.e("CREATE_IUSER", t?.printStackTrace().toString())
                validationErr.postValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<CreateUserResponse>?, response: Response<CreateUserResponse>?) {
                if (response!!.code() == 200) {
                    createUserResponseViewModels.postValue(response.body()!!)
                } else {
                    validationErr.postValue(R.string.error_generic)

                }

            }

        })
    }

    private fun getRequestBody(value: String?): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}