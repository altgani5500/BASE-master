package com.parttime.enterprise.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.parttime.enterprise.apiclients.ApiService
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.addjobs.addJob.AddJobsModelsResponse
import com.parttime.enterprise.uicomman.addjobs.parttimemodel.PartTime
import com.parttime.enterprise.uicomman.fragments.jobslist.currencyModel.CurrancyModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.CoroutineContext

class AddJobViewModel : ViewModel(), CoroutineScope {

    var validationErr = MutableLiveData<Int>()
    var partTimeModelResponse = MutableLiveData<PartTime>()
    var currancyModelResponse = MutableLiveData<CurrancyModel>()
    var addJobsModelsResponse = MutableLiveData<AddJobsModelsResponse>()

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    fun getPartTimeList(lang: String) {
        val apiService = ApiService.initGet()
        val call: Call<PartTime> = apiService.getPartTimeListApi(lang)

        call.enqueue(object : Callback<PartTime> {
            override fun onFailure(call: Call<PartTime>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.setValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<PartTime>?, response: Response<PartTime>?) {
                if (response!!.code() == 200) {
                    partTimeModelResponse.setValue(response.body()!!)
                } else {
                    validationErr.setValue(R.string.error_generic)

                }

            }

        })
    }

    fun getCurrancyList(lang: String) {
        val apiService = ApiService.initGet()
        val call: Call<CurrancyModel> = apiService.getCurrancyApi(lang)

        call.enqueue(object : Callback<CurrancyModel> {
            override fun onFailure(call: Call<CurrancyModel>?, t: Throwable?) {
                Log.e("ERR", call.toString())
                validationErr.setValue(R.string.error_generic)
            }

            override fun onResponse(call: Call<CurrancyModel>?, response: Response<CurrancyModel>?) {
                if (response!!.code() == 200) {
                    currancyModelResponse.setValue(response.body()!!)
                } else {
                    validationErr.setValue(R.string.error_generic)

                }

            }

        })
    }


    /*fun isInputValidJobAdd(
        imgCount: Int,
        uiDesigner: String,
        jobDesc: String,
        location: String,
        branchName: String,
        currancy: String,
        hourlyRateSpn: String,
        hourlyRateEdt: String,
        totalHourweek: String,
        applicant: String,
        hoursOfExp: String,
        userName: String,
        pass: String,
        requirement: String,
        workGuideline: String,
        isConnected: Boolean
    ): Boolean {
        if (imgCount > 0) {
            if (uiDesigner.isNotEmpty()) {
                if (jobDesc.isNotEmpty()) {
                    if (location.isNotEmpty()) {
                        if (branchName.isNotEmpty()) {
                            if (currancy.isNotEmpty()) {
                                if (hourlyRateSpn.isNotEmpty()) {
                                    if (hourlyRateEdt.isNotEmpty()) {
                                        if (totalHourweek.isNotEmpty()) {
                                            if (applicant.isNotEmpty()) {
                                                if (hoursOfExp.isNotEmpty()) {
                                                    if (userName.isNotEmpty()) {
                                                        if (pass.trim().length>=7) {
                                                            if (requirement.isNotEmpty()) {
                                                                if (workGuideline.isNotEmpty()) {
                                                                    if (isConnected) {
                                                                        return true
                                                                    } else {
                                                                        validationErr.setValue(R.string.network_error)
                                                                        validationErr.value=R.string.network_error
                                                                        return false
                                                                    }
                                                                } else {
                                                                    validationErr.setValue(R.string.err_work_guidelines)
                                                                    return false
                                                                }

                                                            } else {
                                                                validationErr.setValue(R.string.err_requirements)
                                                                return false
                                                            }
                                                        } else {
                                                            validationErr.setValue(R.string.error_invalid_password)
                                                            return false
                                                        }
                                                    } else {
                                                        validationErr.setValue(R.string.wify_user_name)
                                                        return false
                                                    }
                                                } else {
                                                    validationErr.setValue(R.string.seek_bar_err)
                                                    return false
                                                }
                                            } else {
                                                validationErr.setValue(R.string.maximul_applicants)
                                                return false
                                            }
                                        } else {
                                            validationErr.setValue(R.string.hourly_rayte_week_select)
                                            return false
                                        }
                                    } else {
                                        validationErr.setValue(R.string.edt_hourly_rate)
                                        return false
                                    }
                                } else {
                                    validationErr.setValue(R.string.hourly_rate_select)
                                    return false
                                }
                            } else {
                                validationErr.setValue(R.string.select_currency)
                                return false
                            }
                        } else {
                            validationErr.setValue(R.string.branch_name_required)
                            return false
                        }
                    } else {
                        validationErr.setValue(R.string.job_location_required)
                        return false
                    }
                } else {
                    validationErr.setValue(R.string.job_description_required)
                    return false
                }
            } else {
                validationErr.setValue(R.string.enter_Job_Title)
                return false
            }
        } else {
            validationErr.setValue(R.string.mimum_images_require)
            return false
        }


        return false

    }
*/
    private fun getRequestBody(value: String?): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value)
    }

    // api for add Jobs
    fun addJobsUserApi(
        hourlyRangeTo:String,
        hasMapCountryStateCity: HashMap<String, RequestBody>,
        imgrayList: ArrayList<File>,
        partTimeType: String,
        uiDesigner: String,
        jobDesc: String,
        location: String,
        branchName: String,
        currancy: String,
        hourlyRateSpn: String,
        hourlyRateEdt: String,
        totalHourweek: String,
        applicant: String,
        hoursOfExp: String,
        userName: String,
        pass: String,
        requirement: String,
        workGuideline: String,
        isConnected: Boolean, flexible: String, sameForAllDays: String, JsonData: String,
        benifits: String, lat: String, lon: String, lang: String, authKey: String
    ) {
        launch {
            var images = ArrayList<MultipartBody.Part>()
            for (i: Int in 0 until imgrayList.size) {
                val image = RequestBody.create(MediaType.parse("image/*"), imgrayList[i])
                images.add(
                    MultipartBody.Part.createFormData(
                        "jobImage[$i]",
                        imgrayList[i].name,
                        image
                    )
                )
                Log.e("IMAGE FOR SERVER", "jobImage[$i]" + imgrayList[i].absoluteFile)
            }
            val map = HashMap<String, RequestBody>()
            if (!hasMapCountryStateCity.isNullOrEmpty()) {
                map.putAll(hasMapCountryStateCity)
            }
            map["jobTitle"] = getRequestBody(uiDesigner)
            map["partTimeTypeId"] = getRequestBody(partTimeType)
            map["jobDescription"] = getRequestBody(jobDesc)
            map["jobLocation"] = getRequestBody(location)
            map["branchName"] = getRequestBody(branchName)
            map["currencyId"] = getRequestBody(currancy)
            map["hours"] = getRequestBody(hourlyRateEdt)
            map["hoursRate"] = getRequestBody(hourlyRateSpn)
            map["totalHoursPerWeek"] = getRequestBody(totalHourweek)
            map["maxApplicants"] = getRequestBody(applicant)
            map["isFlexible"] = getRequestBody(flexible)
            map["minHoursExp"] = getRequestBody(hourlyRangeTo)
            map["maxHoursExp"] = getRequestBody(hoursOfExp)
            map["username"] = getRequestBody(userName)
            map["password"] = getRequestBody(pass)
            map["requirements"] = getRequestBody(requirement)
            map["benifit"] = getRequestBody(benifits)
            map["workGuidence"] = getRequestBody(workGuideline)
            map["workingHours"] = getRequestBody(JsonData)
            map["isSame"] = getRequestBody(sameForAllDays)
            map["jobLong"] = getRequestBody(lon)
            map["jobLat"] = getRequestBody(lat)

            Log.e("ADDJOB DATA REQUEST", map.toString())

            val apiService = ApiService.init()
            val call: Call<AddJobsModelsResponse> = apiService.userAddJob("Bearer $authKey", lang, map, images)

            Log.e("Job Request", call.request().body().toString())
            call.enqueue(object : Callback<AddJobsModelsResponse> {
                override fun onFailure(call: Call<AddJobsModelsResponse>?, t: Throwable?) {
                    Log.e("ADDJOBSERR", t?.localizedMessage + "" + call.toString())
                    validationErr.setValue(R.string.error_generic)
                    validationErr.value = R.string.error_generic
                }

                override fun onResponse(
                    call: Call<AddJobsModelsResponse>?,
                    response: Response<AddJobsModelsResponse>?
                ) {
                    if (response!!.code() == 200) {
                        addJobsModelsResponse.setValue(response.body()!!)
                    } else {
                        validationErr.setValue(R.string.error_generic)

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