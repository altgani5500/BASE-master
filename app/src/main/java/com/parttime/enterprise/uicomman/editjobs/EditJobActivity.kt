package com.parttime.enterprise.uicomman.editjobs

import android.app.Activity
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.darsh.multipleimageselect.activities.AlbumSelectActivity
import com.darsh.multipleimageselect.helpers.Constants
import com.darsh.multipleimageselect.models.Image
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.UploadImagesAdapterEdit
import com.parttime.enterprise.helpers.ImagePickerUtil
import com.parttime.enterprise.helpers.PermissionsUtil
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.addjobs.addJob.AddJobsModelsResponse
import com.parttime.enterprise.uicomman.addjobs.parttimemodel.PartTime
import com.parttime.enterprise.uicomman.editjobs.deleteimage.ImageDeleteResponse
import com.parttime.enterprise.uicomman.editjobs.detailsmodels.EditDetailsData
import com.parttime.enterprise.uicomman.editjobs.detailsmodels.JobImage
import com.parttime.enterprise.uicomman.fragments.jobslist.currencyModel.CurrancyModel
import com.parttime.enterprise.uicomman.jobsdesc.ActivityJobsPreviewEdit
import com.parttime.enterprise.validations.EditJobInputValidation
import com.parttime.enterprise.viewmodels.EditJobViewModels
import com.rtchagas.pingplacepicker.PingPlacePicker
import kotlinx.android.synthetic.main.activity_add_jobs.*
import com.parttime.enterprise.viewutils.toast
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EditJobActivity : BaseActivity(), View.OnClickListener,
    UploadImagesAdapterEdit.OnItemClick {
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private val AUTOCOMPLETE_REQUEST_CODE: Int = 1
    private val pingActivityRequestCode = 10101
    private val JsonArrayPartTimeType = ArrayList<com.parttime.enterprise.uicomman.addjobs.parttimemodel.Message>()
    private val JsonArrayCurrencyType =
        ArrayList<com.parttime.enterprise.uicomman.fragments.jobslist.currencyModel.Message>()

    var fileList = ArrayList<String>()
    private var imageListApi = ArrayList<com.parttime.enterprise.uicomman.editjobs.detailsmodels.JobImage>()
    lateinit var viewModel: EditJobViewModels
    private var partTimeSpinAdapter: ArrayAdapter<String>? = null
    private var currancySpinAdapter: ArrayAdapter<String>? = null


    private lateinit var adapter: UploadImagesAdapterEdit
    private val imgString = ArrayList<String>()
    var counter = 0
    lateinit var partimeDData: String
    lateinit var currenctDDta: String
    var hasMapCountryStateCity = HashMap<String, RequestBody>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_jobs)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            addJobBack.rotation = 180F
           // rangeSeekbarAddjobs.rotation = 180f
        } else {
            addJobBack.rotation = 0F
           // rangeSeekbarAddjobs.rotation = 0f
        }
        jobAddTitle_txt.text = getString(R.string.edit_jobs_me)
        radioGroup.visibility = View.VISIBLE
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.places_key), Locale.US)
        }

        setItemClickListnerInitilise()
        // call api PartTimeType
        getPartTimeListAndSet()
        // currancy Spinner Api Set
        getCurrancyListAndSet()
        // initilise adapter
        setAdapter()
        apiJobDetailsResponse()

        spnHourlyRateAddJobs?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //edtHourlyRateAddjobs.isEnabled = position==5
                if (position == 5) {
                    edtHourlyRateAddjobs.visibility = View.VISIBLE
                } else {
                    edtHourlyRateAddjobs.visibility = View.GONE
                }
            }

        }

    }

    // place picker
    private fun showPlacePicker() {

        val builder = PingPlacePicker.IntentBuilder()

        builder.setAndroidApiKey(getString(R.string.places_key))
            .setMapsApiKey(getString(R.string.places_key))

        // If you want to set a initial location
        // rather then the current device location.
        builder.setLatLng(LatLng(28.628454, 77.376945))

        try {
            val placeIntent = builder.build(this)
            startActivityForResult(placeIntent, pingActivityRequestCode)
        } catch (ex: Exception) {
            ex.printStackTrace()
            toast("Google Play Services is not Available")
        }
    }

    /*On Item Click Listner Initilise*/
    private fun setItemClickListnerInitilise() {
        addJobBack.setOnClickListener(this@EditJobActivity)
        txtStartTimeOne.setOnClickListener(this@EditJobActivity)
        txtEndTimeOne.setOnClickListener(this@EditJobActivity)
        addMoreWorkingHour.setOnClickListener(this@EditJobActivity)
        txtStartTimeTwo.setOnClickListener(this@EditJobActivity)
        txtEndTimeTwo.setOnClickListener(this@EditJobActivity)
        txtStartTimeThree.setOnClickListener(this@EditJobActivity)
        txtEndTimeThree.setOnClickListener(this@EditJobActivity)
        txtStartTimeFour.setOnClickListener(this@EditJobActivity)
        txtEndTimeFour.setOnClickListener(this@EditJobActivity)
        txtStartTimeFive.setOnClickListener(this@EditJobActivity)
        txtEndTimeFive.setOnClickListener(this@EditJobActivity)
        txtStartTimeSix.setOnClickListener(this@EditJobActivity)
        txtEndTimeSix.setOnClickListener(this@EditJobActivity)
        txtStartTimeSeven.setOnClickListener(this@EditJobActivity)
        txtEndTimeSeven.setOnClickListener(this@EditJobActivity)
        //flixable checkbox
        chkFlexible.setOnClickListener(this@EditJobActivity)
        //same for all Days Checkbox
        chkSameForAllDays.setOnClickListener(this@EditJobActivity)
        imgAddJoblayout.setOnClickListener(this@EditJobActivity)

        btnSubmitJobs.setOnClickListener(this@EditJobActivity)
        btnPrevJobs.setOnClickListener(this@EditJobActivity)

        // location
        edtLocationAddjobs.setOnClickListener(this@EditJobActivity)


    }


    // job list get from api
    private fun apiJobDetailsResponse() {
        viewModel.detailsJobListApi(
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
            intent.getStringExtra("JOBID"),
            Utilities.isNetworkAvailable(activity),
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
        )

        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(this@EditJobActivity, addJobBack, getString(msg))
        })
        viewModel.jobDetailsResponseModel1.observe(this, androidx.lifecycle.Observer { response: EditDetailsData ->
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        Log.e("EDITD PAGE", response.toString())
                        edtJobTitleAddJob.setText(response.message.jobTitle)
                        edtJobDescAddjobs.setText(response.message.jobDescription)
                        edtLocationAddjobs.text = response.message.jobLocation
                        edtBranchNameAddJobs.setText(response.message.branchName)
                        edtMaximumApplicantAddjobs.setText(response.message.noOfApplicants)
                        chkFlexible.isChecked = !response.message.isFlexible.contentEquals("0")
                        chkSameForAllDays.isChecked = !response.message.isSame.contentEquals("0")
                        hourlyRangeFrom.setText(response.message.maxHoursExp.toString())
                        edtwifyUserNameAddjobs.setText(response.message.username)
                        edtWifyPasswordAddjobs.setText(response.message.password)
                        edtRequirementsAddJobs.setText(response.message.requirements)
                        hourlyRangeTo.setText(response.message.minHoursExp)
                        edtbenifitsAddJobs.setText(response.message.benifits)
                        edtWorkGuideLineAddJobs.setText(response.message.workGuidence)
                        if(response.message.jobLat.isNotEmpty() && response.message.jobLong.isNotEmpty()) {
                            lat = response.message.jobLat.toDouble()
                            lon = response.message.jobLong.toDouble()
                        }else {
                            lat=0.0
                            lon=0.0
                        }
                        if (!response.message.hoursRates.trim().isNullOrEmpty()) {
                            edtHourlyRateAddjobs.visibility = View.VISIBLE
                            edtHourlyRateAddjobs.setText(response.message.hoursePer)
                        }
                        if (response.message.jobStatus.contentEquals("Active")) {
                            rdbOpen.isChecked = true
                            rdbkepp.isChecked = false
                        } else {
                            rdbkepp.isChecked = true
                            rdbOpen.isChecked = false
                        }

                        partimeDData = response.message.parttimeType
                        currenctDDta = response.message.currency
                        when (response.message.hoursRates.toString().trim()) {
                            "1-5" -> {
                                edtHourlyRateAddjobs.visibility = View.GONE
                                spnHourlyRateAddJobs.setSelection(0)
                            }
                            "5-10" -> {
                                edtHourlyRateAddjobs.visibility = View.GONE
                                spnHourlyRateAddJobs.setSelection(1)
                            }
                            "11-15" -> {
                                edtHourlyRateAddjobs.visibility = View.GONE
                                spnHourlyRateAddJobs.setSelection(2)
                            }
                            "16-20" -> {
                                edtHourlyRateAddjobs.visibility = View.GONE
                                spnHourlyRateAddJobs.setSelection(3)
                            }
                            "21+" -> {
                                edtHourlyRateAddjobs.visibility = View.GONE
                                spnHourlyRateAddJobs.setSelection(4)
                            }
                            "21" -> {
                                edtHourlyRateAddjobs.visibility = View.GONE
                                spnHourlyRateAddJobs.setSelection(4)
                            }
                            "None Of These" -> {
                                spnHourlyRateAddJobs.setSelection(5)
                                edtHourlyRateAddjobs.visibility = View.VISIBLE
                            }
                        }
                        // spnTotalHourWeekAddJobs.setSelection(response.message.totalHoursPerWeek.toInt() - 1)
                        for (i in 0 until JsonArrayPartTimeType.size) {
                            val item = JsonArrayPartTimeType[i]
                            if (item.type.contentEquals(partimeDData)) {
                                spnPartTime.setSelection(i)
                            }
                            // Your code here
                        }
                        for (i in 0 until JsonArrayCurrencyType.size) {
                            val item = JsonArrayCurrencyType[i]
                            if (item.currency.contentEquals(currenctDDta)) {
                                spnCurrancy.setSelection(i)
                            }
                        }
                        for (i: Int in 0 until response.message.editWorkingHours.size) {
                            if (i == 0) {
                                txtStartTimeOne.text = response.message.editWorkingHours[0].startTime
                                txtEndTimeOne.text = response.message.editWorkingHours[0].endTime
                                when (response.message.editWorkingHours[0].day) {
                                    "Sun" -> spnDayOne.setSelection(0)
                                    "Sunday" -> spnDayOne.setSelection(0)
                                    "Mon" -> spnDayOne.setSelection(1)
                                    "Monday" -> spnDayOne.setSelection(1)
                                    "Tues" -> spnDayOne.setSelection(2)
                                    "Tuesday" -> spnDayOne.setSelection(2)
                                    "Wed" -> spnDayOne.setSelection(3)
                                    "Wednesday" -> spnDayOne.setSelection(3)
                                    "Thu" -> spnDayOne.setSelection(4)
                                    "Thursday" -> spnDayOne.setSelection(4)
                                    "Fri" -> spnDayOne.setSelection(5)
                                    "Friday" -> spnDayOne.setSelection(5)
                                    "Sat" -> spnDayOne.setSelection(6)
                                    "Saturday" -> spnDayOne.setSelection(6)
                                }
                                llday1.visibility = View.VISIBLE
                            } else if (i == 1) {
                                txtStartTimeTwo.text = response.message.editWorkingHours[1].startTime
                                txtEndTimeTwo.text = response.message.editWorkingHours[1].endTime
                                when (response.message.editWorkingHours[1].day) {
                                    "Sun" -> spnDayTwo.setSelection(0)
                                    "Sunday" -> spnDayTwo.setSelection(0)
                                    "Mon" -> spnDayTwo.setSelection(1)
                                    "Monday" -> spnDayTwo.setSelection(1)
                                    "Tues" -> spnDayTwo.setSelection(2)
                                    "Tuesday" -> spnDayTwo.setSelection(2)
                                    "Wed" -> spnDayTwo.setSelection(3)
                                    "Wednesday" -> spnDayTwo.setSelection(3)
                                    "Thu" -> spnDayTwo.setSelection(4)
                                    "Thursday" -> spnDayTwo.setSelection(4)
                                    "Fri" -> spnDayTwo.setSelection(5)
                                    "Friday" -> spnDayTwo.setSelection(5)
                                    "Sat" -> spnDayTwo.setSelection(6)
                                    "Saturday" -> spnDayTwo.setSelection(6)

                                }
                                llday2.visibility = View.VISIBLE
                            } else if (i == 2) {
                                txtStartTimeThree.text = response.message.editWorkingHours[2].startTime
                                txtEndTimeThree.text = response.message.editWorkingHours[2].endTime
                                when (response.message.editWorkingHours[2].day) {
                                    "Sun" -> spnDayThree.setSelection(0)
                                    "Sunday" -> spnDayThree.setSelection(0)
                                    "Mon" -> spnDayThree.setSelection(1)
                                    "Monday" -> spnDayThree.setSelection(1)
                                    "Tues" -> spnDayThree.setSelection(2)
                                    "Tuesday" -> spnDayThree.setSelection(2)
                                    "Wed" -> spnDayThree.setSelection(3)
                                    "Wednesday" -> spnDayThree.setSelection(3)
                                    "Thu" -> spnDayThree.setSelection(4)
                                    "Thursday" -> spnDayThree.setSelection(4)
                                    "Fri" -> spnDayThree.setSelection(5)
                                    "Friday" -> spnDayThree.setSelection(5)
                                    "Sat" -> spnDayThree.setSelection(6)
                                    "Saturday" -> spnDayThree.setSelection(6)
                                }
                                llday3.visibility = View.VISIBLE
                            } else if (i == 3) {
                                txtStartTimeFour.text = response.message.editWorkingHours[3].startTime
                                txtEndTimeFour.text = response.message.editWorkingHours[3].endTime
                                when (response.message.editWorkingHours[3].day) {
                                    "Sun" -> spnDayFour.setSelection(0)
                                    "Sunday" -> spnDayFour.setSelection(0)
                                    "Mon" -> spnDayFour.setSelection(1)
                                    "Monday" -> spnDayFour.setSelection(1)
                                    "Tues" -> spnDayFour.setSelection(2)
                                    "Tuesday" -> spnDayFour.setSelection(2)
                                    "Wed" -> spnDayFour.setSelection(3)
                                    "Wednesday" -> spnDayFour.setSelection(3)
                                    "Thu" -> spnDayFour.setSelection(4)
                                    "Thursday" -> spnDayFour.setSelection(4)
                                    "Fri" -> spnDayFour.setSelection(5)
                                    "Friday" -> spnDayFour.setSelection(5)
                                    "Sat" -> spnDayFour.setSelection(6)
                                    "Saturday" -> spnDayFour.setSelection(6)

                                }
                                llday4.visibility = View.VISIBLE
                            } else if (i == 4) {
                                txtStartTimeFive.text = response.message.editWorkingHours[4].startTime
                                txtEndTimeFive.text = response.message.editWorkingHours[4].endTime
                                when (response.message.editWorkingHours[4].day) {
                                    "Sun" -> spnDayFive.setSelection(0)
                                    "Sunday" -> spnDayFive.setSelection(0)
                                    "Mon" -> spnDayFive.setSelection(1)
                                    "Monday" -> spnDayFive.setSelection(1)
                                    "Tues" -> spnDayFive.setSelection(2)
                                    "Tuesday" -> spnDayFive.setSelection(2)
                                    "Wed" -> spnDayFive.setSelection(3)
                                    "Wednesday" -> spnDayFive.setSelection(3)
                                    "Thu" -> spnDayFive.setSelection(4)
                                    "Thursday" -> spnDayFive.setSelection(4)
                                    "Fri" -> spnDayFive.setSelection(5)
                                    "Friday" -> spnDayFive.setSelection(5)
                                    "Sat" -> spnDayFive.setSelection(6)
                                    "Saturday" -> spnDayFive.setSelection(6)

                                }
                                llday5.visibility = View.VISIBLE
                            } else if (i == 5) {
                                txtStartTimeSix.text = response.message.editWorkingHours[5].startTime
                                txtEndTimeSix.text = response.message.editWorkingHours[5].endTime
                                when (response.message.editWorkingHours[5].day) {
                                    "Sun" -> spnDaySix.setSelection(0)
                                    "Sunday" -> spnDaySix.setSelection(0)
                                    "Mon" -> spnDaySix.setSelection(1)
                                    "Monday" -> spnDaySix.setSelection(1)
                                    "Tues" -> spnDaySix.setSelection(2)
                                    "Tuesday" -> spnDaySix.setSelection(2)
                                    "Wed" -> spnDaySix.setSelection(3)
                                    "Wednesday" -> spnDaySix.setSelection(3)
                                    "Thu" -> spnDaySix.setSelection(4)
                                    "Thursday" -> spnDaySix.setSelection(4)
                                    "Fri" -> spnDaySix.setSelection(5)
                                    "Friday" -> spnDaySix.setSelection(5)
                                    "Sat" -> spnDaySix.setSelection(6)
                                    "Saturday" -> spnDaySix.setSelection(6)

                                }
                                llday6.visibility = View.VISIBLE
                            } else if (i == 6) {
                                txtStartTimeSeven.text = response.message.editWorkingHours[6].startTime
                                txtEndTimeSeven.text = response.message.editWorkingHours[6].endTime
                                when (response.message.editWorkingHours[6].day) {
                                    "Sun" -> spnDaySeven.setSelection(0)
                                    "Sunday" -> spnDaySeven.setSelection(0)
                                    "Mon" -> spnDaySeven.setSelection(1)
                                    "Monday" -> spnDaySeven.setSelection(1)
                                    "Tues" -> spnDaySeven.setSelection(2)
                                    "Tuesday" -> spnDaySeven.setSelection(2)
                                    "Wed" -> spnDaySeven.setSelection(3)
                                    "Wednesday" -> spnDaySeven.setSelection(3)
                                    "Thu" -> spnDaySeven.setSelection(4)
                                    "Thursday" -> spnDaySeven.setSelection(4)
                                    "Fri" -> spnDaySeven.setSelection(5)
                                    "Friday" -> spnDaySeven.setSelection(5)
                                    "Sat" -> spnDaySeven.setSelection(6)
                                    "Saturday" -> spnDaySeven.setSelection(6)

                                }
                                llday7.visibility = View.VISIBLE
                            }
                        }

                        for (i: Int in 0 until response.message.jobImages.size) {
                            fileList.add(response.message.jobImages[i].jobImage)
                            imageListApi.add(response.message.jobImages[i])
                        }
                        adapter.notifyDataSetChanged()

                    } else {
                        activity.showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())

            }
        })


    }


    /*Ui hide pick example Logic For time add click*/
    private fun setViewVisibility(count: Int) {
        when (count) {
            1 -> {
                if (!llday2.isVisible) {
                    llday2.visibility = View.VISIBLE
                }
            }
            2 -> {
                if (!llday3.isVisible) {
                    llday3.visibility = View.VISIBLE
                }
            }
            3 -> {
                if (!llday4.isVisible) {
                    llday4.visibility = View.VISIBLE
                }
            }
            4 -> {
                if (!llday5.isVisible) {
                    llday5.visibility = View.VISIBLE
                }
            }
            5 -> {
                if (!llday6.isVisible) {
                    llday6.visibility = View.VISIBLE
                }
            }
            6 -> {
                if (!llday7.isVisible) {
                    llday7.visibility = View.VISIBLE
                }
            }

        }
        //   var s: String = count.toString()
        //   showMessage(activity,llday1,s)

    }


    /*On Item Click*/
    override fun onClick(v: View?) {
        if (v == addJobBack) onBackPressed()
        else if (v == edtLocationAddjobs) {
            /* val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
             val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
             startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)*/
            showPlacePicker()
        } else if (v == txtStartTimeOne) getTime(txtStartTimeOne)
        else if (v == txtEndTimeOne) getTime(txtEndTimeOne)
        else if (v == addMoreWorkingHour) {
            if (counter <= 6) {
                counter++
                setViewVisibility(counter)
            }
        } else if (v == txtStartTimeTwo) getTime(txtStartTimeTwo)
        else if (v == txtStartTimeThree) getTime(txtStartTimeThree)
        else if (v == txtStartTimeFour) getTime(txtStartTimeFour)
        else if (v == txtStartTimeFive) getTime(txtStartTimeFive)
        else if (v == txtStartTimeSix) getTime(txtStartTimeSix)
        else if (v == txtStartTimeSeven) getTime(txtStartTimeSeven)
        else if (v == txtEndTimeTwo) getTime(txtEndTimeTwo)
        else if (v == txtEndTimeThree) getTime(txtEndTimeThree)
        else if (v == txtEndTimeFour) getTime(txtEndTimeFour)
        else if (v == txtEndTimeFive) getTime(txtEndTimeFive)
        else if (v == txtEndTimeSix) getTime(txtEndTimeSix)
        else if (v == txtEndTimeSeven) getTime(txtEndTimeSeven)
        //flexible
        else if (v == chkFlexible) {
            if (chkFlexible.isChecked) {
                spnDayOne.isClickable = false
                spnDayTwo.isClickable = false
                spnDayThree.isClickable = false
                spnDayFour.isClickable = false
                spnDayFive.isClickable = false
                spnDaySix.isClickable = false
                spnDaySeven.isClickable = false
                addMoreWorkingHour.isClickable = false
                txtStartTimeOne.isClickable = false
                txtEndTimeOne.isClickable = false
                addMoreWorkingHour.isClickable = false
                txtStartTimeTwo.isClickable = false
                txtEndTimeTwo.isClickable = false
                txtStartTimeThree.isClickable = false
                txtEndTimeThree.isClickable = false
                txtStartTimeFour.isClickable = false
                txtEndTimeFour.isClickable = false
                txtStartTimeFive.isClickable = false
                txtEndTimeFive.isClickable = false
                txtStartTimeSix.isClickable = false
                txtEndTimeSix.isClickable = false
                txtStartTimeSeven.isClickable = false
                txtEndTimeSeven.isClickable = false
                // chkSameForAllDays.isChecked=false
            } else {
                spnDayOne.isClickable = true
                spnDayTwo.isClickable = true
                spnDayThree.isClickable = true
                spnDayFour.isClickable = true
                spnDayFive.isClickable = true
                spnDaySix.isClickable = true
                spnDaySeven.isClickable = true
                addMoreWorkingHour.isClickable = true
                txtStartTimeOne.isClickable = true
                txtEndTimeOne.isClickable = true
                addMoreWorkingHour.isClickable = true
                txtStartTimeTwo.isClickable = true
                txtEndTimeTwo.isClickable = true
                txtStartTimeThree.isClickable = true
                txtEndTimeThree.isClickable = true
                txtStartTimeFour.isClickable = true
                txtEndTimeFour.isClickable = true
                txtStartTimeFive.isClickable = true
                txtEndTimeFive.isClickable = true
                txtStartTimeSix.isClickable = true
                txtEndTimeSix.isClickable = true
                txtStartTimeSeven.isClickable = true
                txtEndTimeSeven.isClickable = true
                // chkSameForAllDays.isChecked=false
            }
        }
        // same for all Days
        else if (v == chkSameForAllDays) {
            if (chkSameForAllDays.isChecked) {
                spnDayOne.isClickable = false
                spnDayTwo.isClickable = false
                spnDayThree.isClickable = false
                spnDayFour.isClickable = false
                spnDayFive.isClickable = false
                spnDaySix.isClickable = false
                spnDaySeven.isClickable = false
                chkFlexible.isClickable = true
                addMoreWorkingHour.isClickable = false
                txtStartTimeOne.isClickable = false
                txtEndTimeOne.isClickable = false
                addMoreWorkingHour.isClickable = false
                txtStartTimeTwo.isClickable = false
                txtEndTimeTwo.isClickable = false
                txtStartTimeThree.isClickable = false
                txtEndTimeThree.isClickable = false
                txtStartTimeFour.isClickable = false
                txtEndTimeFour.isClickable = false
                txtStartTimeFive.isClickable = false
                txtEndTimeFive.isClickable = false
                txtStartTimeSix.isClickable = false
                txtEndTimeSix.isClickable = false
                txtStartTimeSeven.isClickable = false
                txtEndTimeSeven.isClickable = false
            } else {
                spnDayOne.isClickable = true
                spnDayTwo.isClickable = true
                spnDayThree.isClickable = true
                spnDayFour.isClickable = true
                spnDayFive.isClickable = true
                spnDaySix.isClickable = true
                spnDaySeven.isClickable = true
                chkFlexible.isClickable = true
                addMoreWorkingHour.isClickable = true
                txtStartTimeOne.isClickable = true
                txtEndTimeOne.isClickable = true
                addMoreWorkingHour.isClickable = true
                txtStartTimeTwo.isClickable = true
                txtEndTimeTwo.isClickable = true
                txtStartTimeThree.isClickable = true
                txtEndTimeThree.isClickable = true
                txtStartTimeFour.isClickable = true
                txtEndTimeFour.isClickable = true
                txtStartTimeFive.isClickable = true
                txtEndTimeFive.isClickable = true
                txtStartTimeSix.isClickable = true
                txtEndTimeSix.isClickable = true
                txtStartTimeSeven.isClickable = true
                txtEndTimeSeven.isClickable = true
            }
        } else if (v == imgAddJoblayout) openImagePicker()
        // submit Button functionality
        else if (v == btnSubmitJobs) {
            if(hourlyRangeTo.text.toString().trim().isEmpty()){
                hourlyRangeTo.setText("0")
            }
            if(hourlyRangeFrom.text.toString().trim().isEmpty()){
                hourlyRangeFrom.setText("0")
            }
            if (fileList.size > 0) {
                if (getPartTimeId() > 0) {
                    if (getCurrencyId() >= 0) {
                        if (spnHourlyRateAddJobs.selectedItemPosition >= 0) {
                            if (!spnHourlyRateAddJobs.selectedItem.toString().contentEquals("None Of These")) {
                                edtHourlyRateAddjobs.setText(" ")
                            }
                          //  if (spnTotalHourWeekAddJobs.selectedItemPosition >= 0) {
                                var checkBoxFlex: String? = "0"
                                var sameOfCheckd: String? = "0"
                                checkBoxFlex = if (chkFlexible.isChecked) {
                                    "1"
                                } else {
                                    "0"
                                }
                                if (chkSameForAllDays.isChecked) {
                                    sameOfCheckd = "1"
                                } else {
                                    sameOfCheckd = "0"
                                }
                                var status: String = "Active"
                                if (rdbOpen.isChecked) {
                                    status = "Active"
                                } else if (rdbkepp.isChecked) {
                                    status = "Closed"
                                } else {
                                    status = "Active"
                                }
                                val errInputHandler = EditJobInputValidation().isInputValidJobEditJobs(
                                    hourlyRangeTo.text.toString().trim(),
                                    this@EditJobActivity,
                                    edtJobTitleAddJob.text.toString(),
                                    edtJobDescAddjobs.text.toString(),
                                    edtLocationAddjobs.text.toString(),
                                    edtBranchNameAddJobs.text.toString(),
                                    getCurrencyId().toString(),
                                    spnHourlyRateAddJobs.selectedItem.toString(),
                                    edtHourlyRateAddjobs.text.toString(),
                                    spnTotalHourWeekAddJobs.selectedItem.toString(),
                                    edtMaximumApplicantAddjobs.text.toString(),
                                    hourlyRangeFrom.text.trim().toString(),
                                    edtwifyUserNameAddjobs.text.toString(),
                                    edtWifyPasswordAddjobs.text.toString(),
                                    edtRequirementsAddJobs.text.toString(),
                                    edtWorkGuideLineAddJobs.text.toString(),
                                    Utilities.isNetworkAvailable(this@EditJobActivity)
                                )
                                if (sameOfCheckd.contentEquals("1") && checkBoxFlex.contentEquals("1")) {
                                    showMessage(
                                        this@EditJobActivity,
                                        edtJobTitleAddJob,
                                        getString(R.string.select_either_flex_or_same)
                                    )
                                } else {
                                    if (checkBoxFlex.contentEquals("0")) {
                                        if (txtStartTimeOne.text.toString().isEmpty()) {
                                            showMessage(
                                                this@EditJobActivity,
                                                edtJobTitleAddJob,
                                                getString(R.string.select_start_time)
                                            )
                                        } else {
                                            if (txtEndTimeOne.text.toString().isEmpty()) {
                                                showMessage(
                                                    this@EditJobActivity,
                                                    edtJobTitleAddJob,
                                                    getString(R.string.select_end_time)
                                                )
                                            } else {
                                                if (errInputHandler.status == 1) {
                                                    apiAddJobCallandGetResponse(
                                                        hourlyRangeTo.text.trim().toString(),
                                                        hasMapCountryStateCity,
                                                        fileList,
                                                        getPartTimeId().toString(),
                                                        edtJobTitleAddJob.text.toString(),
                                                        edtJobDescAddjobs.text.toString(),
                                                        edtLocationAddjobs.text.toString(),
                                                        edtBranchNameAddJobs.text.toString(),
                                                        getCurrencyId().toString(),
                                                        spnHourlyRateAddJobs.selectedItem.toString(),
                                                        edtHourlyRateAddjobs.text.toString(),
                                                        spnTotalHourWeekAddJobs.selectedItem.toString(),
                                                        edtMaximumApplicantAddjobs.text.toString(),
                                                        hourlyRangeFrom.text.trim().toString(),
                                                        edtwifyUserNameAddjobs.text.toString(),
                                                        edtWifyPasswordAddjobs.text.toString(),
                                                        edtRequirementsAddJobs.text.toString(),
                                                        edtWorkGuideLineAddJobs.text.toString(),
                                                        Utilities.isNetworkAvailable(activity),
                                                        checkBoxFlex,
                                                        sameOfCheckd,
                                                        getWorkingHourDynamicJsonArray(),
                                                        edtbenifitsAddJobs.text.toString(),
                                                        lat.toString(),
                                                        lon.toString(),
                                                        appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                                                        appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                                        intent.getStringExtra("JOBID"), status

                                                    )
                                                } else {
                                                    showMessage(
                                                        this@EditJobActivity,
                                                        edtJobTitleAddJob,
                                                        errInputHandler.errMessage
                                                    )
                                                }
                                            }
                                        }

                                    } else {
                                        if (sameOfCheckd.contentEquals("1")) {
                                            if (txtStartTimeOne.text.toString().isEmpty()) {
                                                showMessage(
                                                    this@EditJobActivity,
                                                    edtJobTitleAddJob,
                                                    getString(R.string.select_start_time)
                                                )
                                            } else {
                                                if (txtEndTimeOne.text.toString().isEmpty()) {
                                                    showMessage(
                                                        this@EditJobActivity,
                                                        edtJobTitleAddJob,
                                                        getString(R.string.select_end_time)
                                                    )
                                                } else {
                                                    if (errInputHandler.status == 1) {
                                                        apiAddJobCallandGetResponse(
                                                            hourlyRangeTo.text.trim().toString(),
                                                            hasMapCountryStateCity,
                                                            fileList,
                                                            getPartTimeId().toString(),
                                                            edtJobTitleAddJob.text.toString(),
                                                            edtJobDescAddjobs.text.toString(),
                                                            edtLocationAddjobs.text.toString(),
                                                            edtBranchNameAddJobs.text.toString(),
                                                            getCurrencyId().toString(),
                                                            spnHourlyRateAddJobs.selectedItem.toString(),
                                                            edtHourlyRateAddjobs.text.toString(),
                                                            spnTotalHourWeekAddJobs.selectedItem.toString(),
                                                            edtMaximumApplicantAddjobs.text.toString(),
                                                            hourlyRangeFrom.text.trim().toString(),
                                                            edtwifyUserNameAddjobs.text.toString(),
                                                            edtWifyPasswordAddjobs.text.toString(),
                                                            edtRequirementsAddJobs.text.toString(),
                                                            edtWorkGuideLineAddJobs.text.toString(),
                                                            Utilities.isNetworkAvailable(activity),
                                                            checkBoxFlex,
                                                            sameOfCheckd,
                                                            getWorkingHourDynamicJsonArray(),
                                                            edtbenifitsAddJobs.text.toString(),
                                                            lat.toString(),
                                                            lon.toString(),
                                                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                                                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                                            intent.getStringExtra("JOBID"), status

                                                        )
                                                    } else {
                                                        showMessage(
                                                            this@EditJobActivity,
                                                            edtJobTitleAddJob,
                                                            errInputHandler.errMessage
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            if (errInputHandler.status == 1) {
                                                apiAddJobCallandGetResponse(
                                                    hourlyRangeTo.text.trim().toString(),
                                                    hasMapCountryStateCity,
                                                    fileList,
                                                    getPartTimeId().toString(),
                                                    edtJobTitleAddJob.text.toString(),
                                                    edtJobDescAddjobs.text.toString(),
                                                    edtLocationAddjobs.text.toString(),
                                                    edtBranchNameAddJobs.text.toString(),
                                                    getCurrencyId().toString(),
                                                    spnHourlyRateAddJobs.selectedItem.toString(),
                                                    edtHourlyRateAddjobs.text.toString(),
                                                    spnTotalHourWeekAddJobs.selectedItem.toString(),
                                                    edtMaximumApplicantAddjobs.text.toString(),
                                                    hourlyRangeFrom.text.trim().toString(),
                                                    edtwifyUserNameAddjobs.text.toString(),
                                                    edtWifyPasswordAddjobs.text.toString(),
                                                    edtRequirementsAddJobs.text.toString(),
                                                    edtWorkGuideLineAddJobs.text.toString(),
                                                    Utilities.isNetworkAvailable(activity),
                                                    checkBoxFlex,
                                                    sameOfCheckd,
                                                    getWorkingHourDynamicJsonArray(),
                                                    edtbenifitsAddJobs.text.toString(),
                                                    lat.toString(),
                                                    lon.toString(),
                                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                                    intent.getStringExtra("JOBID"), status

                                                )
                                            } else {
                                                showMessage(
                                                    this@EditJobActivity,
                                                    edtJobTitleAddJob,
                                                    errInputHandler.errMessage
                                                )
                                            }
                                        }
                                    }

                                }


                          //  } else {
                           //     showMessage(this@EditJobActivity, spnPartTime, getString(R.string.select_hour_of_week))
                           // }
                        } else {
                            showMessage(
                                this@EditJobActivity,
                                spnPartTime,
                                getString(R.string.please_select_hourly_rate)
                            )
                        }

                    } else {
                        showMessage(this@EditJobActivity, spnPartTime, getString(R.string.please_select_currency))
                    }
                } else {
                    showMessage(this@EditJobActivity, spnPartTime, getString(R.string.please_select_parttimetype))
                }
            } else {
                showMessage(this@EditJobActivity, spnPartTime, getString(R.string.mimum_images_require))
            }


        }

        // prev btn
        if (v == btnPrevJobs) {
            var intent = Intent(this@EditJobActivity, ActivityJobsPreviewEdit::class.java)
            intent.putExtra("FILE_LIST", fileList)
            intent.putExtra("PARTTIME_TYPE", spnPartTime.selectedItem.toString())
            intent.putExtra("JOB_TITLE", edtJobTitleAddJob.text.toString())
            intent.putExtra("JOB_DESC", edtJobDescAddjobs.text.toString())
            intent.putExtra("JOB_LOCATION", edtLocationAddjobs.text.toString())
            intent.putExtra("BRANCH_NAME", edtBranchNameAddJobs.text.toString())
            intent.putExtra("CURRENCY", spnCurrancy.selectedItem.toString())
            intent.putExtra("SPN_HOURLY_RATE", spnHourlyRateAddJobs.selectedItem.toString())
            intent.putExtra("EDT_HOURLY_RATE", edtHourlyRateAddjobs.text.toString())
            intent.putExtra("HOURLY_WEEK_TOTAL", spnTotalHourWeekAddJobs.selectedItem.toString())
            intent.putExtra("MAX_APPLICANT", edtMaximumApplicantAddjobs.text.toString())
            if(!hourlyRangeTo.text.toString().isNullOrEmpty() && !hourlyRangeFrom.text.toString().isNullOrEmpty())
            intent.putExtra("RANGESEEKBAR",hourlyRangeTo.text.toString()+"-"+hourlyRangeFrom.text.trim().toString())
            else
            intent.putExtra("RANGESEEKBAR"," ")

            intent.putExtra("REQUIREMENTS", edtRequirementsAddJobs.text.toString())
            intent.putExtra("GUIDELINES", edtWorkGuideLineAddJobs.text.toString())
            intent.putExtra("WORKING_HOUR", getWorkingHourDynamicJsonArray())
            intent.putExtra("BENIFITS", edtbenifitsAddJobs.text.toString())
            intent.putExtra("LAT", lat)
            intent.putExtra("LON", lon)
            startActivity(intent)


        }

    }

    private fun apiAddJobCallandGetResponse(
        hourlyRangeTos:String,
        hasMapCountryStateCity: HashMap<String, RequestBody>,
        fileList: ArrayList<String>,
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
        benifits: String, lat: String, lon: String, lang: String, authKey: String, jobId: String, jobStatus: String

    ) {
        var newImgLis = ArrayList<File>()
        for (i in 0 until fileList.size) {
            if (!fileList[i].contains("http")) {
                newImgLis.add(File(fileList[i].toString()))
            }
        }
        showProgressBarNew()
        viewModel.updateJobsUserApi(
            hourlyRangeTos,
            hasMapCountryStateCity,
            newImgLis,
            partTimeType,
            uiDesigner,
            jobDesc,
            location,
            branchName,
            currancy,
            hourlyRateSpn,
            hourlyRateEdt,
            totalHourweek,
            applicant,
            hoursOfExp,
            userName,
            pass,
            requirement,
            workGuideline,
            isConnected, flexible, sameForAllDays, JsonData,
            benifits, lat, lon, lang, authKey, jobId, jobStatus
        )

        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@EditJobActivity, btnSubmitJobs, getString(msg))
        })
        viewModel.updateJobStatus.observe(this, androidx.lifecycle.Observer { response: AddJobsModelsResponse ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    showMessage(this@EditJobActivity, btnSubmitJobs, response.message.toString())
                    //launchActivityMain(HomeActivity::class.java)
                    onBackPressed()
                } else if (response.code == 204) {
                    hideProgressBarNew()
                    showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                } else if (response.code == 401) {
                    hideProgressBarNew()
                    showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())

            }
        })

    }


    /*Get Part Time Type From Api*/
    fun getPartTimeListAndSet() {
        viewModel = ViewModelProviders.of(this@EditJobActivity).get(EditJobViewModels::class.java)
        viewModel.getPartTimeList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            showMessage(this@EditJobActivity, addJobBack, getString(msg))
        })

        viewModel.partTimeModelResponse1.observe(this, androidx.lifecycle.Observer { response: PartTime ->
            if (response.success) {
                if (response.code == 200) {
                    // Success Response
                    var list: MutableList<String> = mutableListOf<String>()
                    JsonArrayPartTimeType.addAll(response.message)
                    val itr = response.message.iterator()
                    while (itr.hasNext()) {
                        list.add(itr.next().type)
                        // arrayListPartimeType.add(itr.next().parttimeTypeId)

                    }
                    partTimeSpinAdapter = ArrayAdapter<String>(this, R.layout.spinner_item_text, R.id.spinText, list)
                    //partTimeSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnPartTime.adapter = partTimeSpinAdapter
                } else if (response.code == 204) {
                    showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                } else if (response.code == 401) {
                    showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                }
            } else {
                showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
            }
        })
    }

    /*Get Currency From Api*/
    fun getCurrancyListAndSet() {
        viewModel = ViewModelProviders.of(this@EditJobActivity).get(EditJobViewModels::class.java)
        viewModel.getCurrancyList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            showMessage(this@EditJobActivity, addJobBack, getString(msg))
        })

        viewModel.currancyModelResponse1.observe(this, androidx.lifecycle.Observer { response: CurrancyModel ->
            if (response.success) {
                if (response.code == 200) {
                    // Success Response
                    var list: MutableList<String> = mutableListOf<String>()
                    JsonArrayCurrencyType.addAll(response.message)
                    val itr = response.message.iterator()
                    while (itr.hasNext()) {
                        list.add(itr.next().currency)
                    }
                    currancySpinAdapter = ArrayAdapter<String>(this, R.layout.spinner_item_text, R.id.spinText, list)
                    spnCurrancy.adapter = currancySpinAdapter
                } else if (response.code == 204) {
                    showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                } else if (response.code == 401) {
                    showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                }
            } else {
                showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())

            }
        })
    }


    private fun getCurrencyId(): Int {
        var currentTypeid: Int = 0
        for (i in 0 until JsonArrayCurrencyType.size) {
            val item = JsonArrayCurrencyType[i]
            if (item.currency == spnCurrancy.selectedItem) {
                currentTypeid = item.currencyId
            }

        }
        return currentTypeid


    }

    // convert jsonArray For Working Hour
    private fun getWorkingHourDynamicJsonArray(): String {
        if (chkFlexible.isChecked) {
            return "1"
        } else {
            if (chkSameForAllDays.isChecked) {
                val jsonArry = JSONArray()
                if (llday1.isVisible) {
                    val jsObject = JSONObject()
                    jsObject.put("day", spnDayOne.selectedItem.toString())
                    jsObject.put("startTime", txtStartTimeOne.text.toString())
                    jsObject.put("endTime", txtEndTimeOne.text.toString())
                    jsonArry.put(jsObject)
                }
                return jsonArry.toString()
            } else {
                val jsonArry = JSONArray()
                if (llday1.isVisible) {
                    val jsObject = JSONObject()
                    jsObject.put("day", spnDayOne.selectedItem.toString())
                    jsObject.put("startTime", txtStartTimeOne.text.toString())
                    jsObject.put("endTime", txtEndTimeOne.text.toString())
                    jsonArry.put(jsObject)
                }
                if (llday2.isVisible) {
                    val jsObject = JSONObject()
                    jsObject.put("day", spnDayTwo.selectedItem.toString())
                    jsObject.put("startTime", txtStartTimeTwo.text.toString())
                    jsObject.put("endTime", txtEndTimeTwo.text.toString())
                    jsonArry.put(jsObject)
                }

                if (llday3.isVisible) {
                    val jsObject = JSONObject()
                    jsObject.put("day", spnDayThree.selectedItem.toString())
                    jsObject.put("startTime", txtStartTimeThree.text.toString())
                    jsObject.put("endTime", txtEndTimeThree.text.toString())
                    jsonArry.put(jsObject)
                }

                if (llday4.isVisible) {
                    val jsObject = JSONObject()
                    jsObject.put("day", spnDayFour.selectedItem.toString())
                    jsObject.put("startTime", txtStartTimeFour.text.toString())
                    jsObject.put("endTime", txtEndTimeFour.text.toString())
                    jsonArry.put(jsObject)
                }

                if (llday5.isVisible) {
                    val jsObject = JSONObject()
                    jsObject.put("day", spnDayFive.selectedItem.toString())
                    jsObject.put("startTime", txtStartTimeFive.text.toString())
                    jsObject.put("endTime", txtEndTimeFive.text.toString())
                    jsonArry.put(jsObject)
                }

                if (llday6.isVisible) {
                    val jsObject = JSONObject()
                    jsObject.put("day", spnDaySix.selectedItem.toString())
                    jsObject.put("startTime", txtStartTimeSix.text.toString())
                    jsObject.put("endTime", txtEndTimeSix.text.toString())
                    jsonArry.put(jsObject)
                }

                if (llday7.isVisible) {
                    val jsObject = JSONObject()
                    jsObject.put("day", spnDaySeven.selectedItem.toString())
                    jsObject.put("startTime", txtStartTimeSeven.text.toString())
                    jsObject.put("endTime", txtEndTimeSeven.text.toString())
                    jsonArry.put(jsObject)
                }

                return jsonArry.toString()
            }

        }

    }


    //Time Picker
    fun getTime(textView: TextView) {

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            //textView.text = SimpleDateFormat("HH:mm a").format(cal.time)
            var d24HourSDF = SimpleDateFormat("HH:mm")
            var d12HourSDF = SimpleDateFormat("hh:mm a");
            var getTempDate = SimpleDateFormat("HH:mm").format(cal.time)
            var _24HourDt = d24HourSDF.parse(getTempDate);
            textView.text = d12HourSDF.format(_24HourDt)
        }

        textView.setOnClickListener {
            TimePickerDialog(
                this@EditJobActivity,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    /*Method to set recyclerview adapter
    */
    private fun setAdapter() {
        adapter = UploadImagesAdapterEdit(this@EditJobActivity, fileList, this@EditJobActivity)
        recycle_image.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recycle_image.adapter = adapter

    }

    /*
   Method to open image picker
    */
    private fun openImagePicker() {
        PermissionsUtil.askPermissions(
            this@EditJobActivity,
            PermissionsUtil.CAMERA,
            PermissionsUtil.STORAGE,
            object : PermissionsUtil.PermissionListener {
                override fun onPermissionResult(isGranted: Boolean) {
                    if (isGranted) {
                        val items = arrayOf<CharSequence>(
                            activity.getString(R.string.camera),
                            activity.getString(R.string.gallery)
                        )
                        val builder = AlertDialog.Builder(this@EditJobActivity)
                        builder.setTitle(null)
                        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
                            if (items[item] == activity.getString(R.string.camera)) {
                                ImagePickerUtil.pickFromCamera(
                                    this@EditJobActivity as Activity,
                                    object : ImagePickerUtil.ImagePickerListener {
                                        override fun onImagePicked(imageFile: File, tag: String) {
                                            fileList.add(compressFile(imageFile).absolutePath.toString())
                                            adapter.notifyDataSetChanged()
                                        }
                                    },
                                    "img" + Random().nextInt(), true
                                )
                            } else if (items[item] == activity.getString(R.string.gallery)) {
                                val intent = Intent(this@EditJobActivity, AlbumSelectActivity::class.java)
                                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5 - fileList.size)
                                startActivityForResult(intent, Constants.REQUEST_CODE)
                            }
                        })
                        builder.show()
                    }
                }

            })

    }

    private fun compressFile(file: File): File {
        var compressedFile = file
        var outputStream: FileOutputStream? = null
        val bm = ImagePickerUtil.decodeFile(compressedFile.absolutePath, 800, 800, ImagePickerUtil.ScalingLogic.FIT)

        try {
            outputStream = FileOutputStream(compressedFile)
            bm.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return compressedFile

    }


    override fun onBackPressed() {
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                lat = place.latLng?.latitude!!
                lon = place.latLng?.longitude!!
                var name: String = place.name.toString()
                edtLocationAddjobs.text = place.name
                Log.e("PLACE NAME", name)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                var status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("address", status.statusMessage)
            }
        }
        if ((requestCode == pingActivityRequestCode) && (resultCode == Activity.RESULT_OK)) {

            val place: Place? = PingPlacePicker.getPlace(data!!)
            lat = place?.latLng?.latitude!!
            lon = place.latLng?.longitude!!
            edtLocationAddjobs.text = place.name
            var state = place.address?.split(",")
            if (!state.isNullOrEmpty()) {
                Collections.reverse(state)
                for (i in 0 until state.size) {
                    if (i == 0) {
                        hasMapCountryStateCity.put("country", getRequestBody(state[0]))
                    }
                    if (i == 1) {
                        hasMapCountryStateCity.put(
                            "state",
                            getRequestBody(activity.getStringInStringWithNumber(state[1]))
                        )
                    }
                    if (i == 2) {
                        hasMapCountryStateCity.put("city", getRequestBody(state[2]))
                    }
                }
                Log.e("ADDRRESS-VALUE", hasMapCountryStateCity.toString())
            }


            //  toast("You selected: ${place?.name}"+lat.toString()+lon.toString()+place?.address)
        }
        if (requestCode == Constants.REQUEST_CODE && resultCode == BaseActivity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Constants.INTENT_EXTRA_IMAGES)
            for (myImages in images) {
                fileList.add(File(myImages.path).absolutePath.toString())
            }

            if (fileList.size >= 5) {
                imgAddJoblayout.visibility = View.GONE
            } else {
                imgAddJoblayout.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()

        } else {
            ImagePickerUtil.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun getRequestBody(value: String?): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value)
    }

    // recycle View Item Click Listner
    override fun onClickImages(value: Int) {
        openImagePicker()
    }


    /*Get Part Time Id*/
    private fun getPartTimeId(): Int {
        var parTimeType: Int = 0
        for (i in 0 until JsonArrayPartTimeType.size) {
            val item = JsonArrayPartTimeType[i]
            if (item.type == spnPartTime.selectedItem) {
                parTimeType = item.parttimeTypeId
            }
            // Your code here
        }
        return parTimeType
    }

    // remove Images From Api
    override fun onRemoveImages(str: String) {
        if (str.contains("http")) {
            for (i in 0 until imageListApi.size) {
                if (imageListApi[i].jobImage.contentEquals(str)) {
                    delImageThroughApit(imageListApi[i].jobImageId.toString(), str, imageListApi[i])
                }
            }

        } else {
            fileList.remove(str)
            adapter.notifyDataSetChanged()
        }

    }


    // delete Image Api Hit and respose
    fun delImageThroughApit(id: String, str: String, jobImg: JobImage) {
        viewModel = ViewModelProviders.of(this@EditJobActivity).get(EditJobViewModels::class.java)
        showProgressBarNew()
        viewModel.deleteJobImageApi(
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
            intent.getStringExtra("JOBID"),
            Utilities.isNetworkAvailable(activity),
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
        )

        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@EditJobActivity, addJobBack, getString(msg))
        })

        viewModel.imageDelResponseModel.observe(this, androidx.lifecycle.Observer { response: ImageDeleteResponse ->
            if (response.success) {
                hideProgressBarNew()
                if (response.code == 200) {
                    // Success Response
                    showMessage(this@EditJobActivity, addJobBack, response.message.toString())
                    imageListApi.remove(jobImg)
                    fileList.remove(str)
                    adapter.notifyDataSetChanged()
                    adapter.notifyDataSetChanged()
                } else if (response.code == 204) {
                    showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                    hideProgressBarNew()
                } else if (response.code == 401) {
                    showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                    hideProgressBarNew()
                }
            } else {
                showMessage(this@EditJobActivity, addJobBack, response.errorMessage.toString())
                hideProgressBarNew()

            }
        })
    }


}