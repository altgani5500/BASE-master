package com.parttime.enterprise.uicomman.locationaddress

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.CityListAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.helpers.Utilities.isNetworkAvailable
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.locationaddress.citymodel.CityModelResponseX
import com.parttime.enterprise.uicomman.locationaddress.citymodel.MessageX
import com.parttime.enterprise.viewmodels.CityListViewModel
import kotlinx.android.synthetic.main.activity_country_layout.*

class CityListActivity : BaseActivity(), CityListAdapter.ItemClickListener, View.OnClickListener {


    // lateinit var viewModel: CountryListViewModel
    private var countrySelet = " "
    private var msgList = ArrayList<com.parttime.enterprise.uicomman.locationaddress.citymodel.MessageX>()
    lateinit var mAdapter: CityListAdapter
    lateinit var layoutManager: LinearLayoutManager
    private var listSelectedItem = ArrayList<String>()
    lateinit var viewModel: CityListViewModel

    override fun onClick(p0: View?) {
        when (p0) {
            filterBack_locationa -> onBackPressed()
            jobAddTitle_txt3 -> recreate()
            cancel_Country -> {
                input_messageSearch.setText(" ")
                if (msgList.isNullOrEmpty()) {
                    msgList.clear()
                    mAdapter.notifyDataSetChanged()
                }
                setAdapter()
            }
        }
    }


    override fun onItemClicked(cities: ArrayList<MessageX>) {
        if (!listSelectedItem.isNullOrEmpty()) {
            listSelectedItem.clear()
        }
        for (i in 0 until cities.size) {
            for (j in 0 until cities[i].cityList.size)
                if (cities[i].cityList[j].isClicked) {
                    listSelectedItem.add(cities[i].cityList[j].city)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_layout)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            filterBack_locationa.rotation = 180F
        } else {
            filterBack_locationa.rotation = 0F
        }

        inItView()
        setAdapter()

        // edit search functionality
        input_messageSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //toast(message = "Number is $s")
                mAdapter.filter.filter(s)
            }
        })

        // filter appliy
        btnCountryloc_filter.setOnClickListener {
            if (listSelectedItem.isNullOrEmpty()) {
                listSelectedItem.clear()
            }
            listSelectedItem.add(countrySelet)

            val intent = Intent(this@CityListActivity, ActivityLocationFilter::class.java)
            intent.putExtra("CITY", listSelectedItem)
            intent.putExtra("FLAG", "4")
            setResult(103, intent)
            finish()
        }
    }

    private fun inItView() {
        filterBack_locationa.setOnClickListener(this@CityListActivity)
        jobAddTitle_txt3.setOnClickListener(this@CityListActivity)
        cancel_Country.setOnClickListener(this@CityListActivity)
    }

    /*
     Method to set recyclerview adapter
     */
    private fun setAdapter() {
        // initilise api
        if (!intent.getStringExtra("STATE").isNullOrEmpty()) {
            if (Utilities.isNetworkAvailable(this@CityListActivity)) {
                showProgressBarNew()
                viewModel = ViewModelProviders.of(activity).get(CityListViewModel::class.java)
                viewModel.getcityList(
                    activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                    activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                    intent.getStringExtra("STATE"),
                    isNetworkAvailable(activity)
                )
                stateListResponse()
            } else {
                showMessage(this@CityListActivity, recycleCountry, getString(R.string.network_error))
            }
        }

    }

    // state list get from api
    private fun stateListResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(this@CityListActivity, recycleCountry, getString(msg))
        })
        viewModel.cityResponse.observe(this, Observer { response: CityModelResponseX ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {

                    if (!response.message.isNullOrEmpty()) {
                        for (i in 0 until response.message.size) {
                            for (j in 0 until response.message[i].cityList.size)
                                response.message[i].cityList[j].isClicked = false
                        }
                        msgList = response.message
                        mAdapter = CityListAdapter(activity, msgList, this@CityListActivity)
                        recycleCountry.adapter = mAdapter
                        layoutManager = LinearLayoutManager(activity)
                        recycleCountry.layoutManager = layoutManager
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(this@CityListActivity, recycleCountry, response.errorMessage.toString())

                } else {
                    activity.hideProgressBarNew()
                    activity.showMessage(this@CityListActivity, recycleCountry, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(this@CityListActivity, recycleCountry, response.errorMessage.toString())
            }
        })

    }


    override fun onBackPressed() {
        val intent = Intent(this@CityListActivity, ActivityLocationFilter::class.java)
        intent.putExtra("FLAG", "1")
        /*intent.putExtra("PREV_SETTING",PREV_SETTING_FOR_AUTO);*/
        setResult(101, intent)
        finish()
    }
}