package com.parttime.enterprise.uicomman.addjobs

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.darsh.multipleimageselect.activities.AlbumSelectActivity
import com.darsh.multipleimageselect.helpers.Constants
import com.darsh.multipleimageselect.models.Image
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.parttime.enterprise.BuildConfig.APPLICATION_ID
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.UploadImagesAdapter
import com.parttime.enterprise.helpers.ImagePickerUtil
import com.parttime.enterprise.helpers.PermissionsUtil
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.helpers.Utilities.isNetworkAvailable
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.addjobs.addJob.AddJobsModelsResponse
import com.parttime.enterprise.uicomman.addjobs.parttimemodel.PartTime
import com.parttime.enterprise.uicomman.fragments.jobslist.currencyModel.CurrancyModel
import com.parttime.enterprise.uicomman.jobsdesc.ActivityJobsPreview
import com.parttime.enterprise.validations.AdddJojInputValidation
import com.parttime.enterprise.viewmodels.AddJobViewModel
import com.rtchagas.pingplacepicker.PingPlacePicker
import com.rtchagas.pingplacepicker.ui.PlacePickerActivity
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


class AddJobsActivity : BaseActivity(), View.OnClickListener,
    UploadImagesAdapter.OnItemClick {
    var lat: Double = 0.0
    var lon: Double = 0.0
    val AUTOCOMPLETE_REQUEST_CODE: Int = 1
    private val pingActivityRequestCode = 1001
    val JsonArrayPartTimeType = ArrayList<com.parttime.enterprise.uicomman.addjobs.parttimemodel.Message>()
    val JsonArrayCurrencyType = ArrayList<com.parttime.enterprise.uicomman.fragments.jobslist.currencyModel.Message>()
    var fileList = ArrayList<File>()
    lateinit var viewModel: AddJobViewModel
    lateinit var viewModel2: AddJobViewModel
    var partTimeSpinAdapter: ArrayAdapter<String>? = null
    var currancySpinAdapter: ArrayAdapter<String>? = null
    private lateinit var adapter: UploadImagesAdapter
    // private val imgString = ArrayList<String>()
    var hasMapCountryStateCity = HashMap<String, RequestBody>()
    var counter = 0

    // for gps
    private val TAG = "MainActivity"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_jobs)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            addJobBack.rotation = 180F
            //rangeSeekbarAddjobs.rotation = 180f
            // rangeSeekbarAddjobs.textDirection = 0
        } else {
            addJobBack.rotation = 0F
           // rangeSeekbarAddjobs.rotation = 0f
            //rangeSeekbarAddjobs.textDirection = 0
        }
        //Google place Initialized
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.places_key), Locale.US)
        }
        setItemClickListnerInitilise()
        // call api PartTimeType
        getPartTimeListAndSet()
        // currency Spinner Api Set
        getCurrancyListAndSet()
        // initialise adapter
        setAdapter()
        spnHourlyRateAddJobs?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 5) {
                    edtHourlyRateAddjobs.visibility = View.VISIBLE
                } else {
                    edtHourlyRateAddjobs.visibility = View.GONE
                }
                // edtHourlyRateAddjobs.isEnabled = position == 5
            }
        }

        //  dummy click listnetr
        jobAddTitle_txt.setOnClickListener {
            //launchActivity(MapsActivity::class.java)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    // place picker
    private fun showPlacePicker() {
        PingPlacePicker.isNearbySearchEnabled = resources.getBoolean(R.bool.show_confirmation_photo)
        PlacePickerActivity
        PlacePickerActivity
        val builder = PingPlacePicker.IntentBuilder()
        builder.setAndroidApiKey(getString(R.string.places_key))
            .setMapsApiKey(getString(R.string.places_key))
        // If you want to set a initial location
        // rather then the current device location.
        // pingBuilder.setLatLng(LatLng(37.4219999, -122.0862462))

        try {
            val placeIntent = builder.build(this)
            startActivityForResult(placeIntent, pingActivityRequestCode)
        } catch (ex: Exception) {
            toast("Google Play Services is not Available")
        }
    }

    override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    // for gps
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    // latitudeText.text = resources
                    //     .getString(R.string.latitude_label, task.result.latitude)
                    //  longitudeText.text = resources
                    //     .getString(R.string.longitude_label, task.result.longitude)
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    showMessage(this@AddJobsActivity, jobAddTitle_txt, getString(R.string.gps_on))
                }
            }
    }

    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(R.string.permission_rationale, android.R.string.ok, View.OnClickListener {
                // Request permission
                startLocationPermissionRequest()
            })

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }


    private fun showSnackbar(
        snackStrId: Int,
        actionStrId: Int = 0,
        listener: View.OnClickListener? = null
    ) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content), getString(snackStrId),
            LENGTH_INDEFINITE
        )
        if (actionStrId != 0 && listener != null) {
            snackbar.setAction(getString(actionStrId), listener)
        }
        snackbar.show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                grantResults.isEmpty() -> Log.i(TAG, "User interaction was cancelled.")
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> getLastLocation()
                else -> {
                    showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", APPLICATION_ID, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        })
                }
            }
        } else {
            PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /*On Item Click Listener Initialize*/
    private fun setItemClickListnerInitilise() {
        addJobBack.setOnClickListener(this@AddJobsActivity)
        txtStartTimeOne.setOnClickListener(this@AddJobsActivity)
        txtEndTimeOne.setOnClickListener(this@AddJobsActivity)
        addMoreWorkingHour.setOnClickListener(this@AddJobsActivity)
        txtStartTimeTwo.setOnClickListener(this@AddJobsActivity)
        txtEndTimeTwo.setOnClickListener(this@AddJobsActivity)
        txtStartTimeThree.setOnClickListener(this@AddJobsActivity)
        txtEndTimeThree.setOnClickListener(this@AddJobsActivity)
        txtStartTimeFour.setOnClickListener(this@AddJobsActivity)
        txtEndTimeFour.setOnClickListener(this@AddJobsActivity)
        txtStartTimeFive.setOnClickListener(this@AddJobsActivity)
        txtEndTimeFive.setOnClickListener(this@AddJobsActivity)
        txtStartTimeSix.setOnClickListener(this@AddJobsActivity)
        txtEndTimeSix.setOnClickListener(this@AddJobsActivity)
        txtStartTimeSeven.setOnClickListener(this@AddJobsActivity)
        txtEndTimeSeven.setOnClickListener(this@AddJobsActivity)
        //flixable checkbox
        chkFlexible.setOnClickListener(this@AddJobsActivity)
        //same for all Days Checkbox
        chkSameForAllDays.setOnClickListener(this@AddJobsActivity)
        imgAddJoblayout.setOnClickListener(this@AddJobsActivity)

        btnSubmitJobs.setOnClickListener(this@AddJobsActivity)
        btnPrevJobs.setOnClickListener(this@AddJobsActivity)

        // location
        edtLocationAddjobs.setOnClickListener(this@AddJobsActivity)

    }

    /*Get Part Time Type From Api*/
    fun getPartTimeListAndSet() {
        viewModel = ViewModelProviders.of(this@AddJobsActivity).get(AddJobViewModel::class.java)
        viewModel.getPartTimeList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this@AddJobsActivity, Observer { msg: Int ->
            showMessage(this@AddJobsActivity, addJobBack, getString(msg))
        })
        viewModel.partTimeModelResponse.observe(this@AddJobsActivity, Observer { response: PartTime ->
            if (response.success) {
                if (response.code == 200) {
                    // Success Response
                    var list: MutableList<String> = mutableListOf<String>()
                    JsonArrayPartTimeType.addAll(response.message)
                    val itr = response.message.iterator()
                    while (itr.hasNext()) {
                        list.add(itr.next().type)

                    }
                    partTimeSpinAdapter = ArrayAdapter<String>(this, R.layout.spinner_item_text, R.id.spinText, list)
                    //partTimeSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnPartTime.adapter = partTimeSpinAdapter
                } else if (response.code == 204) {
                    showMessage(this@AddJobsActivity, addJobBack, response.errorMessage.toString())
                } else if (response.code == 401) {
                    showMessage(this@AddJobsActivity, addJobBack, response.errorMessage.toString())
                }
            } else {
                showMessage(this@AddJobsActivity, addJobBack, response.errorMessage.toString())

            }
        })

    }

    /*Get Currency From Api*/
    fun getCurrancyListAndSet() {

        viewModel = ViewModelProviders.of(this@AddJobsActivity).get(AddJobViewModel::class.java)
        viewModel.getCurrancyList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this@AddJobsActivity, Observer { msg: Int ->

            showMessage(this@AddJobsActivity, addJobBack, getString(msg))
        })
        viewModel.currancyModelResponse.observe(this@AddJobsActivity, Observer { response: CurrancyModel ->

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
                    showMessage(this@AddJobsActivity, addJobBack, response.errorMessage.toString())
                } else if (response.code == 401) {
                    showMessage(this@AddJobsActivity, addJobBack, response.errorMessage.toString())
                }
            } else {
                showMessage(this@AddJobsActivity, addJobBack, response.errorMessage.toString())
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
            /* val fields = asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
             val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
             startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)*/
            showPlacePicker()
        } else if (v == txtStartTimeOne) getTime(txtStartTimeOne, "", 1)
        else if (v == txtEndTimeOne) getTime(txtEndTimeOne, txtStartTimeOne.text.toString(), 2)
        else if (v == addMoreWorkingHour) {
            if (counter <= 6) {
                counter++
                setViewVisibility(counter)
            }
        } else if (v == txtStartTimeTwo) getTime(txtStartTimeTwo, "", 1)
        else if (v == txtStartTimeThree) getTime(txtStartTimeThree, "", 1)
        else if (v == txtStartTimeFour) getTime(txtStartTimeFour, "", 1)
        else if (v == txtStartTimeFive) getTime(txtStartTimeFive, "", 1)
        else if (v == txtStartTimeSix) getTime(txtStartTimeSix, "", 1)
        else if (v == txtStartTimeSeven) getTime(txtStartTimeSeven, "", 1)
        else if (v == txtEndTimeTwo) getTime(txtEndTimeTwo, txtStartTimeTwo.text.toString(), 2)
        else if (v == txtEndTimeThree) getTime(txtEndTimeThree, txtStartTimeThree.text.toString(), 2)
        else if (v == txtEndTimeFour) getTime(txtEndTimeFour, txtStartTimeFour.text.toString(), 2)
        else if (v == txtEndTimeFive) getTime(txtEndTimeFive, txtStartTimeFive.text.toString(), 2)
        else if (v == txtEndTimeSix) getTime(txtEndTimeSix, txtStartTimeSix.text.toString(), 2)
        else if (v == txtEndTimeSeven) getTime(txtEndTimeSeven, txtStartTimeSeven.text.toString(), 2)
        //flexible
        else if (v == chkFlexible) {
            if (chkFlexible.isChecked) {
                chkSameForAllDays.isChecked = false
                //chkSameForAllDays.isClickable=false
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
            }
        }
        // same for all Days
        else if (v == chkSameForAllDays) {
            if (chkSameForAllDays.isChecked) {
                chkFlexible.isChecked = false
                //chkFlexible.isClickable=false
                spnDayOne.isClickable = false
                spnDayTwo.isClickable = false
                spnDayThree.isClickable = false
                spnDayFour.isClickable = false
                spnDayFive.isClickable = false
                spnDaySix.isClickable = false
                spnDaySeven.isClickable = false
                chkFlexible.isClickable = false
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

            if (getPartTimeId() > 0) {
                if (getCurrencyId() >= 0) {

                    if (spnHourlyRateAddJobs.selectedItemPosition >= 0) {
                     //   if (spnTotalHourWeekAddJobs.selectedItemPosition >= 0) {
                            var checkBoxFlex: String? = "0"
                            var sameOfCheckd: String? = "0"
                            if (chkFlexible.isChecked) {
                                checkBoxFlex = "1"
                            } else {
                                checkBoxFlex = "0"
                            }
                            if (chkSameForAllDays.isChecked) {
                                sameOfCheckd = "1"
                            } else {
                                sameOfCheckd = "0"
                            }
                            val errInputHandler = AdddJojInputValidation().isInputValidJobAddJobs(
                                hourlyRangeTo.text.trim().toString(),
                                this@AddJobsActivity,
                                fileList.size,
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
                                isNetworkAvailable(this@AddJobsActivity)
                            )
                            if (sameOfCheckd.contentEquals("1") && checkBoxFlex.contentEquals("1")) {
                                showMessage(
                                    this@AddJobsActivity,
                                    edtJobTitleAddJob,
                                    getString(R.string.select_either_flex_or_same)
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
                                        checkBoxFlex,
                                        sameOfCheckd,
                                        getWorkingHourDynamicJsonArray(),
                                        edtbenifitsAddJobs.text.toString(),
                                        lat.toString(),
                                        lon.toString(),
                                        appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                                        appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
                                    )
                                } else {
                                    showMessage(this@AddJobsActivity, edtJobTitleAddJob, errInputHandler.errMessage)
                                }
                            }
                       // } else {
                       //     showMessage(this@AddJobsActivity, spnPartTime, getString(R.string.select_hour_of_week))
                       // }
                    } else {
                        showMessage(this@AddJobsActivity, spnPartTime, getString(R.string.please_select_hourly_rate))
                    }

                } else {
                    showMessage(this@AddJobsActivity, spnPartTime, getString(R.string.please_select_currency))
                }
            } else {
                showMessage(this@AddJobsActivity, spnPartTime, getString(R.string.please_select_parttimetype))
            }

        }

        // prev btn
        if (v == btnPrevJobs) {
            var intent = Intent(this@AddJobsActivity, ActivityJobsPreview::class.java)
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
            if(!hourlyRangeFrom.text.toString().trim().isNullOrEmpty() && !hourlyRangeTo.text.toString().trim().isNullOrEmpty())
            intent.putExtra("RANGESEEKBAR", hourlyRangeTo.text.toString().trim()+"-"+hourlyRangeFrom.text.trim().toString())
            else{
                intent.putExtra("RANGESEEKBAR", " ")
            }
            intent.putExtra("REQUIREMENTS", edtRequirementsAddJobs.text.toString())
            intent.putExtra("GUIDELINES", edtWorkGuideLineAddJobs.text.toString())
            intent.putExtra("WORKING_HOUR", getWorkingHourDynamicJsonArray())
            intent.putExtra("BENIFITS", edtbenifitsAddJobs.text.toString())
            intent.putExtra("LAT", lat)
            intent.putExtra("LON", lon)
            startActivity(intent)


        }

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
    fun getTime(textView: TextView, prevTime: String, flag: Int) {
        if (flag == 2) {
            if (prevTime.trim().isNotEmpty()) {
                val cal = Calendar.getInstance()
                var count = 0
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    var d24HourSDF = SimpleDateFormat("HH:mm")
                    var d12HourSDF = SimpleDateFormat("hh:mm a");
                    var getTempDate = SimpleDateFormat("HH:mm").format(cal.time)
                    var _24HourDt = d24HourSDF.parse(getTempDate);
                    textView.text = d12HourSDF.format(_24HourDt)
                    if (textView.text.toString().trim().contentEquals(prevTime.trim())) {
                        textView.text = ""
                        Toast.makeText(
                            this@AddJobsActivity,
                            getString(R.string.select_date_time_same),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                textView.setOnClickListener {
                    var v = TimePickerDialog(
                        this@AddJobsActivity,
                        timeSetListener,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        false
                    ).show()

                }

            } else {
                Toast.makeText(this@AddJobsActivity, getString(R.string.select_start_time), Toast.LENGTH_LONG).show()
                return
            }
        } else {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                //var dte:Date=SimpleDateFormat("HH:mm a").format(cal.time)
                var d24HourSDF = SimpleDateFormat("HH:mm")
                var d12HourSDF = SimpleDateFormat("hh:mm a");
                var getTempDate = SimpleDateFormat("HH:mm").format(cal.time)
                var _24HourDt = d24HourSDF.parse(getTempDate);
                textView.text = d12HourSDF.format(_24HourDt)
            }
            textView.setOnClickListener {
                TimePickerDialog(
                    this@AddJobsActivity,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                ).show()
            }
        }
    }

    /*Method to set recyclerview adapter
    */
    private fun setAdapter() {
        adapter = UploadImagesAdapter(this@AddJobsActivity, fileList, this@AddJobsActivity)
        recycle_image.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recycle_image.adapter = adapter

    }

    /*
   Method to open image picker
    */
    private fun openImagePicker() {
        PermissionsUtil.askPermissions(
            this@AddJobsActivity,
            PermissionsUtil.CAMERA,
            PermissionsUtil.STORAGE,
            object : PermissionsUtil.PermissionListener {
                override fun onPermissionResult(isGranted: Boolean) {
                    if (isGranted) {
                        val items = arrayOf<CharSequence>(
                            activity.getString(R.string.camera),
                            activity.getString(R.string.gallery)
                        )
                        val builder = AlertDialog.Builder(this@AddJobsActivity)
                        builder.setTitle(null)
                        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
                            if (items[item] == activity.getString(R.string.camera)) {
                                ImagePickerUtil.pickFromCamera(
                                    this@AddJobsActivity as Activity,
                                    object : ImagePickerUtil.ImagePickerListener {
                                        override fun onImagePicked(imageFile: File, tag: String) {
                                            fileList.add(compressFile(imageFile))
                                            adapter.notifyDataSetChanged()
                                        }
                                    },
                                    "img" + Random().nextInt(), true
                                )
                            } else if (items[item] == activity.getString(R.string.gallery)) {
                                val intent = Intent(this@AddJobsActivity, AlbumSelectActivity::class.java)
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
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Constants.INTENT_EXTRA_IMAGES)
            for (myImages in images) {
                fileList.add(File(myImages.path))
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


    // add api call
    /*  Method to implement viewmodel*/
    private fun apiAddJobCallandGetResponse(
        hourlyRangeTos:String,
        hasMapCountryStateCity: HashMap<String, RequestBody>,
        imgrayList1: ArrayList<File>,
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
        userName1: String,
        pass: String,
        requirement: String,
        workGuideline: String,
        flexible: String, sameForAllDays: String, JsonData: String,
        benifits: String, lat: String, lon: String, lang: String, authKey: String
    ) {
        viewModel2 = ViewModelProviders.of(this@AddJobsActivity).get(AddJobViewModel::class.java)
        showProgressBarNew()

        viewModel2.addJobsUserApi(
            hourlyRangeTos,
            hasMapCountryStateCity,
            imgrayList1,
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
            userName1,
            pass,
            requirement,
            workGuideline,
            Utilities.isNetworkAvailable(this@AddJobsActivity), flexible, sameForAllDays, JsonData,
            benifits, lat, lon, lang, authKey
        )
        viewModel2.addJobsModelsResponse.observe(this@AddJobsActivity, Observer { response: AddJobsModelsResponse ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    showMessage(this@AddJobsActivity, btnSubmitJobs, response.message.toString())
                    onBackPressed()
                } else if (response.code == 204) {
                    hideProgressBarNew()
                    showMessage(this@AddJobsActivity, addJobBack, response.errorMessage.toString())
                } else if (response.code == 401) {
                    hideProgressBarNew()
                    showMessage(this@AddJobsActivity, addJobBack, response.errorMessage.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(this@AddJobsActivity, addJobBack, response.errorMessage.toString())

            }
        })

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


}