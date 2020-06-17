package com.parttime.enterprise.uicomman.locationaddress

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.parttime.enterprise.BuildConfig
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.CityRowListItemAdapter
import com.parttime.enterprise.adapters.RowListItemAdapter
import com.parttime.enterprise.adapters.StateRowListItemAdapter
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.ApplicantFilterActivity
import com.parttime.enterprise.uicomman.locationaddress.services.FetchAddressIntentService
import kotlinx.android.synthetic.main.activity_location_filter.*


class ActivityLocationFilter : BaseActivity(), RowListItemAdapter.RowItemClick,
    StateRowListItemAdapter.StateRowItemClick, CityRowListItemAdapter.CityRowItemClick, View.OnClickListener {

    override fun onClick(p0: View?) {
        when (p0) {
            btnloc_filter1 -> {
                if (listSelectedItem.isNotEmpty()) {
                    val intent = Intent(this@ActivityLocationFilter, ApplicantFilterActivity::class.java)
                    intent.putExtra("SELECTED", listSelectedItem)
                    intent.putExtra("FLAG", "2")
                    intent.putExtra("LAT", lat)
                    intent.putExtra("LON", lon)
                    intent.putExtra("FLAGS", 2)
                    intent.putExtra("PREV_SETTING", PREV_SETTING_FOR_AUTO)
                    setResult(1001, intent)
                    finish()
                } else {
                    val intent = Intent(this@ActivityLocationFilter, ApplicantFilterActivity::class.java)
                    if (!countryListSelected.isNullOrEmpty()
                        && !stateListSelected.isNullOrEmpty()
                        && !cityListSelected.isNullOrEmpty()
                    ) {
                        intent.putExtra("FLAG", "3")
                        intent.putExtra("COUNTRY", countryListSelected)
                        intent.putExtra("STATE", stateListSelected)
                        intent.putExtra("CITY", cityListSelected)
                        intent.putExtra("FLAGS", 3)
                        intent.putExtra("PREV_SETTING", PREV_SETTING_FOR_AUTO)
                        setResult(1001, intent)
                        finish()
                    }
                }
            }
        }
    }


    private val TAG = ActivityLocationFilter::class.java.simpleName
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private val ADDRESS_REQUESTED_KEY = "address-request-pending"
    private val LOCATION_ADDRESS_KEY = "location-address"
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var addressRequested = false
    private var addressOutput = ""
    private lateinit var resultReceiver: AddressResultReceiver
    private lateinit var locationAddressTextView: TextView
    private lateinit var fetchAddressButton: Switch
    private var isGPS = false
    private var listSelectedItem = ArrayList<String>()
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var PREV_SETTING_FOR_AUTO = 0
    // location
    private lateinit var adapter1: RowListItemAdapter
    private var countryListSelected = ArrayList<String>()
    private lateinit var adapterState: StateRowListItemAdapter
    private var stateListSelected = ArrayList<String>()
    private lateinit var adapterCity: CityRowListItemAdapter
    private var cityListSelected = ArrayList<String>()


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_filter)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            filterBack_location.rotation = 180F
        } else {
            filterBack_location.rotation = 0F
        }
        PREV_SETTING_FOR_AUTO = intent.getIntExtra("PREV_SETTING", 0)
        resultReceiver = AddressResultReceiver(Handler())

        locationAddressTextView = findViewById(R.id.locationToggleText)
        fetchAddressButton = findViewById(R.id.locationToggle)

        // Set defaults, then update using values stored in the Bundle.
        addressRequested = false
        addressOutput = ""
        updateValuesFromBundle(savedInstanceState)

        initView()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            GpsUtils(this).turnGPSOn(GpsUtils.onGpsListener {
                isGPS = it
            })
        }
        // set pref Setting for auto detect
        if (PREV_SETTING_FOR_AUTO == 0) {
            fetchAddressButton.isChecked = false
            // The toggle is disabled
            locationAddressTextView.visibility = View.GONE
            ll_City.setBackgroundColor(resources.getColor(R.color.white))
            ll_City.isClickable = true
            ll_state.setBackgroundColor(resources.getColor(R.color.white))
            ll_state.isClickable = true
            ll_country.setBackgroundColor(resources.getColor(R.color.white))
            ll_country.isClickable = true
            PREV_SETTING_FOR_AUTO = 0
            if (!listSelectedItem.isNullOrEmpty()) {
                listSelectedItem.clear()
            }
        } else {
            fetchAddressButton.isChecked = true
            // The toggle is disabled
            locationAddressTextView.visibility = View.VISIBLE
            fetchAddressButtonHandler()
            locationAddressTextView.visibility = View.VISIBLE
            ll_City.setBackgroundColor(resources.getColor(R.color.divider))
            ll_City.isClickable = false
            ll_state.setBackgroundColor(resources.getColor(R.color.divider))
            ll_state.isClickable = false
            ll_country.setBackgroundColor(resources.getColor(R.color.divider))
            ll_country.isClickable = false
            PREV_SETTING_FOR_AUTO = 1
        }

        // reset functionality
        jobAddTitle_txt1.setOnClickListener {
            fetchAddressButton.isChecked = false
            fetchAddressButton.isClickable = true
            locationAddressTextView.visibility = View.GONE
            ll_City.setBackgroundColor(resources.getColor(R.color.white))
            ll_City.isClickable = true
            ll_state.setBackgroundColor(resources.getColor(R.color.white))
            ll_state.isClickable = true
            ll_country.setBackgroundColor(resources.getColor(R.color.white))
            ll_country.isClickable = true
            PREV_SETTING_FOR_AUTO = 0
            if (!listSelectedItem.isNullOrEmpty()) {
                listSelectedItem.clear()
            }
            if (!countryListSelected.isNullOrEmpty()) {
                countryListSelected.clear()
                adapter1.notifyDataSetChanged()
            }
            if (!stateListSelected.isNullOrEmpty()) {
                stateListSelected.clear()
                adapterState.notifyDataSetChanged()
            }
            if (!cityListSelected.isNullOrEmpty()) {
                cityListSelected.clear()
                adapterCity.notifyDataSetChanged()
            }


        }

        // updateUIWidgets()

        fetchAddressButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The toggle is enabled/checked
                fetchAddressButtonHandler()
                locationAddressTextView.visibility = View.VISIBLE
                ll_City.setBackgroundColor(resources.getColor(R.color.divider))
                ll_City.isClickable = false
                ll_state.setBackgroundColor(resources.getColor(R.color.divider))
                ll_state.isClickable = false
                ll_country.setBackgroundColor(resources.getColor(R.color.divider))
                ll_country.isClickable = false
                PREV_SETTING_FOR_AUTO = 1
            } else {
                // The toggle is disabled
                locationAddressTextView.visibility = View.GONE
                ll_City.setBackgroundColor(resources.getColor(R.color.white))
                ll_City.isClickable = true
                ll_state.setBackgroundColor(resources.getColor(R.color.white))
                ll_state.isClickable = true
                ll_country.setBackgroundColor(resources.getColor(R.color.white))
                ll_country.isClickable = true
                PREV_SETTING_FOR_AUTO = 0
                if (!listSelectedItem.isNullOrEmpty()) {
                    listSelectedItem.clear()
                }
                // Set the app background color to gray
                // root_layout.setBackgroundColor(Color.GRAY)
            }


        }

        // on back press
        filterBack_location.setOnClickListener {
            onBackPressed()
        }

        // country filter click
        ll_country.setOnClickListener {
            // disable toggle button
            if (listSelectedItem.isNotEmpty()) {
                listSelectedItem.clear()
            }
            fetchAddressButton.isClickable = false
            locationAddressTextView.visibility = View.GONE
            PREV_SETTING_FOR_AUTO = 0
            val intent = Intent(this@ActivityLocationFilter, CountryListActivity::class.java)
            startActivityForResult(intent, 101)
        }

        ll_City.setOnClickListener {
            // disable toggle button
            PREV_SETTING_FOR_AUTO = 0
            fetchAddressButton.isClickable = false
            locationAddressTextView.visibility = View.GONE
            if (!stateListSelected.isNullOrEmpty() && stateListSelected.size > 0) {
                var value: String = " "
                val sb = StringBuilder()
                for (i in 0 until stateListSelected.size) {
                    sb.append(stateListSelected[i]).append(",")
                }
                value = sb.toString()
                value = value.substring(0, value.length - 1)
                //filterDataMap["education"] = value
                val intent = Intent(this@ActivityLocationFilter, CityListActivity::class.java)
                intent.putExtra("STATE", value)
                startActivityForResult(intent, 103)
            }
        }

        ll_state.setOnClickListener {
            // disable toggle button
            PREV_SETTING_FOR_AUTO = 0
            fetchAddressButton.isClickable = false
            locationAddressTextView.visibility = View.GONE
            if (!countryListSelected.isNullOrEmpty() && countryListSelected.size > 0) {
                val intent = Intent(this@ActivityLocationFilter, StateActivity::class.java)
                intent.putExtra("COUNTRY", countryListSelected[0])
                startActivityForResult(intent, 102)
            }
        }


    }

    override fun onResume() {
        super.onResume()
       /* if(isGPS) {
            if (PREV_SETTING_FOR_AUTO == 1) {
                fetchAddressButtonHandler()
            }else{
                hideProgressBarNew()
            }
        }else{
            hideProgressBarNew()
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (data!!.getStringExtra("FLAG").contentEquals("2")) {
                countryListSelected.addAll(data.getSerializableExtra("COUNTRY") as ArrayList<String>)
                if (countryListSelected.isNullOrEmpty()) {
                    countryListSelected.clear()
                } else {
                    var tempList = countryListSelected.distinct()
                    if (countryListSelected.isNotEmpty()) {
                        countryListSelected.clear()
                        adapter1.notifyDataSetChanged()
                    }
                    countryListSelected.addAll(tempList)
                    if (tempList.isNotEmpty()) {
                        tempList = emptyList()
                    }
                    country_RecycleView.visibility = View.VISIBLE
                    adapter1.notifyDataSetChanged()
                }
            }
        } else if (requestCode == 102) {
            if (data!!.getStringExtra("FLAG").contentEquals("3")) {
                stateListSelected.addAll(data.getSerializableExtra("STATE") as ArrayList<String>)
                if (stateListSelected.isNullOrEmpty()) {
                    stateListSelected.clear()
                } else {
                    stateListSelected.removeAt(stateListSelected.size - 1)
                    var tempList = stateListSelected.distinct()
                    if (stateListSelected.isNotEmpty()) {
                        stateListSelected.clear()
                        adapterState.notifyDataSetChanged()
                    }
                    stateListSelected.addAll(tempList)
                    if (tempList.isNotEmpty()) {
                        tempList = emptyList()
                    }
                    state_RecycleView.visibility = View.VISIBLE
                    adapterState.notifyDataSetChanged()
                }
            }
        } else if (requestCode == 103) {
            if (data!!.getStringExtra("FLAG").contentEquals("4")) {
                cityListSelected.addAll(data.getSerializableExtra("CITY") as ArrayList<String>)
                if (cityListSelected.isNullOrEmpty()) {
                    cityListSelected.clear()
                } else {
                    cityListSelected.removeAt(cityListSelected.size - 1)
                    var tempList = cityListSelected.distinct()
                    if (cityListSelected.isNotEmpty()) {
                        cityListSelected.clear()
                        adapterCity.notifyDataSetChanged()
                    }
                    cityListSelected.addAll(tempList)
                    if (tempList.isNotEmpty()) {
                        tempList = emptyList()
                    }
                    city_RecycleView.visibility = View.VISIBLE
                    adapterState.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initView() {
        // check if prev location selected is it or not
        btnloc_filter1.setOnClickListener(this@ActivityLocationFilter)
        if (countryListSelected.isNotEmpty()) {
            adapter1.notifyDataSetChanged()
        }
        adapter1 = RowListItemAdapter(this@ActivityLocationFilter, countryListSelected, this@ActivityLocationFilter)
        country_RecycleView.layoutManager = GridLayoutManager(this@ActivityLocationFilter, 2)
        country_RecycleView.adapter = adapter1

        if (stateListSelected.isNotEmpty()) {
            adapterState.notifyDataSetChanged()
        }
        adapterState =
            StateRowListItemAdapter(this@ActivityLocationFilter, stateListSelected, this@ActivityLocationFilter)
        state_RecycleView.layoutManager = GridLayoutManager(this@ActivityLocationFilter, 2)
        state_RecycleView.adapter = adapterState

        if (cityListSelected.isNotEmpty()) {
            adapterCity.notifyDataSetChanged()
        }
        adapterCity =
            CityRowListItemAdapter(this@ActivityLocationFilter, cityListSelected, this@ActivityLocationFilter)
        city_RecycleView.layoutManager = GridLayoutManager(this@ActivityLocationFilter, 2)
        city_RecycleView.adapter = adapterCity
    }


    override fun rowItemClick(pos: Int) {
        if (!countryListSelected.isNullOrEmpty()) {
            countryListSelected.drop(pos)
            adapter1.notifyDataSetChanged()
        }
        if (countryListSelected.isNullOrEmpty()) {
            country_RecycleView.visibility = View.GONE
        } else {
            country_RecycleView.visibility = View.VISIBLE
        }
    }

    override fun stateRowItemClick(pos: Int) {
        if (!stateListSelected.isNullOrEmpty()) {
            stateListSelected.drop(pos)
            adapterState.notifyDataSetChanged()
        }
        if (stateListSelected.isNullOrEmpty()) {
            state_RecycleView.visibility = View.GONE
        } else {
            state_RecycleView.visibility = View.VISIBLE
        }
    }

    override fun cityRowItemClick(pos: Int) {
        if (!cityListSelected.isNullOrEmpty()) {
            cityListSelected.drop(pos)
            adapterCity.notifyDataSetChanged()
        }
        if (cityListSelected.isNullOrEmpty()) {
            city_RecycleView.visibility = View.GONE
        } else {
            city_RecycleView.visibility = View.VISIBLE
        }
    }

    public override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getAddress()
        }
    }

    @SuppressLint("MissingPermission")
    private fun obtieneLocalizacion() {
        fusedLocationClient!!.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                }
            }
    }

    /**
     * Updates fields based on data stored in the bundle.
     */
    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState ?: return

        ADDRESS_REQUESTED_KEY.let {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(it)) {
                addressRequested = savedInstanceState.getBoolean(it)
            }
        }

        LOCATION_ADDRESS_KEY.let {
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(it)) {
                addressOutput = savedInstanceState.getString(it)!!
                displayAddressOutput()
            }
        }


    }

    /**
     * Runs when user clicks the Fetch Address button.
     */
    @Suppress("UNUSED_PARAMETER")
    fun fetchAddressButtonHandler() {
        if (lastLocation != null) {
            startIntentService()
            return
        }

        // If we have not yet retrieved the user location, we process the user's request by setting
        // addressRequested to true. As far as the user is concerned, pressing the Fetch Address
        // button immediately kicks off the process of getting the address.
        addressRequested = true
        updateUIWidgets()
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    private fun startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        val intent = Intent(this, FetchAddressIntentService::class.java).apply {
            // Pass the result receiver as an extra to the service.
            putExtra(Constants.RECEIVER, resultReceiver)

            // Pass the location data as an extra to the service.
            putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation)
        }

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent)
    }

    /**
     * Gets the address for the last known location.
     */
    @SuppressLint("MissingPermission")
    private fun getAddress() {
        /*    GpsUtils(this).turnGPSOn(GpsUtils.onGpsListener {
                isGPS = it
            })*/
        if (isGPS) {
            if (!checkPermissions()) {
                requestPermissions()
            } else {
                obtieneLocalizacion()
                fusedLocationClient?.lastLocation?.addOnSuccessListener(this, OnSuccessListener { location ->
                    if (location == null) {
                        Log.w(TAG, "onSuccess:null")
                        return@OnSuccessListener
                    }

                    lastLocation = location

                    // Determine whether a Geocoder is available.
                    if (!Geocoder.isPresent()) {
                        Snackbar.make(
                            findViewById<View>(android.R.id.content),
                            R.string.no_geocoder_available, Snackbar.LENGTH_LONG
                        ).show()
                        return@OnSuccessListener
                    }

                    // If the user pressed the fetch address button before we had the location,
                    // this will be set to true indicating that we should kick off the intent
                    // service after fetching the location.
                    if (addressRequested) startIntentService()
                })?.addOnFailureListener(this) { e -> Log.w(TAG, "getLastLocation:onFailure", e) }
            }

        } else {
            Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show()
            return
        }
    }

    /**
     * Updates the address in the UI.
     */
    private fun displayAddressOutput() {

        locationAddressTextView.text = addressOutput
        if (locationAddressTextView.isVisible) {
            listSelectedItem.add(addressOutput)
        }
    }

    /**
     * Toggles the visibility of the progress bar. Enables or disables the Fetch Address button.
     */
    private fun updateUIWidgets() {
        if (addressRequested) {
            showProgressBarNew()
            fetchAddressButton.isChecked = false
        } else {
            hideProgressBarNew()
            fetchAddressButton.isChecked = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState

        with(outState) {
            // Save whether the address has been requested.
            putBoolean(ADDRESS_REQUESTED_KEY, addressRequested)

            // Save the address string.
            putString(LOCATION_ADDRESS_KEY, addressOutput)
        }

        super.onSaveInstanceState(outState)
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    private inner class AddressResultReceiver internal constructor(
        handler: Handler
    ) : ResultReceiver(handler) {

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            // Display the address string or an error message sent from the intent service.
            addressOutput = resultData.getString(Constants.RESULT_DATA_KEY)!!

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                // Toast.makeText(this@ActivityLocationFilter, R.string.address_found, Toast.LENGTH_SHORT).show()
                displayAddressOutput()
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            addressRequested = false
            updateUIWidgets()
        }
    }

    /**
     * Shows a [Snackbar].
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private fun showSnackbar(
        mainTextStringId: Int,
        actionStringId: Int,
        listener: View.OnClickListener
    ) {
        Snackbar.make(
            findViewById(android.R.id.content), getString(mainTextStringId),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(actionStringId), listener)
            .show()
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val shouldProvideRationale = shouldShowRequestPermissionRationale(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                View.OnClickListener {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@ActivityLocationFilter,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                })

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(
                this@ActivityLocationFilter,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")

        if (requestCode != REQUEST_PERMISSIONS_REQUEST_CODE) return

        when {
            grantResults.isEmpty() ->
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> // Permission granted.
                getAddress()
            else -> // Permission denied.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                    View.OnClickListener {
                        // Build intent that displays the App settings screen.
                        val intent = Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    })
        }

    }


    override fun onBackPressed() {
        val intent = Intent(this@ActivityLocationFilter, ApplicantFilterActivity::class.java)
        intent.putExtra("FLAG", "1")
        setResult(1001, intent)
        finish()
    }

}
