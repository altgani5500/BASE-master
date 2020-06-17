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
import com.parttime.enterprise.adapters.CountryListAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.helpers.Utilities.isNetworkAvailable
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.locationaddress.countrymodel.CountryListResponse
import com.parttime.enterprise.uicomman.locationaddress.countrymodel.Message
import com.parttime.enterprise.viewmodels.CountryListViewModel
import kotlinx.android.synthetic.main.activity_country_layout.*

class CountryListActivity : BaseActivity(), CountryListAdapter.ItemClickListener, View.OnClickListener {

    lateinit var viewModel: CountryListViewModel
    private var countrySelet = " "
    private var msgList = ArrayList<com.parttime.enterprise.uicomman.locationaddress.countrymodel.Message>()
    lateinit var mAdapter: CountryListAdapter
    lateinit var layoutManager: LinearLayoutManager
    private var listSelectedItem = ArrayList<String>()

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

    override fun onItemClicked(country: Message) {
        countrySelet = country.country
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
            if(!countrySelet.trim().isNullOrEmpty()) {
                listSelectedItem.add(countrySelet)

                val intent = Intent(this@CountryListActivity, ActivityLocationFilter::class.java)
                intent.putExtra("COUNTRY", listSelectedItem)
                intent.putExtra("FLAG", "2")
                setResult(101, intent)
                finish()
            }
        }
    }

    private fun inItView() {
        filterBack_locationa.setOnClickListener(this@CountryListActivity)
        jobAddTitle_txt3.setOnClickListener(this@CountryListActivity)
        cancel_Country.setOnClickListener(this@CountryListActivity)
    }

    /*
     Method to set recyclerview adapter
     */
    private fun setAdapter() {
        // initilise api
        if (Utilities.isNetworkAvailable(this@CountryListActivity)) {
            showProgressBarNew()
            viewModel = ViewModelProviders.of(activity).get(CountryListViewModel::class.java)
            viewModel.getCountryList(
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                isNetworkAvailable(activity)
            )
            countryResponse()
        } else {
            showMessage(this@CountryListActivity, recycleCountry, getString(R.string.network_error))
        }

    }

    // job list get from api
    private fun countryResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(this@CountryListActivity, recycleCountry, getString(msg))
        })
        viewModel.countryResponse.observe(this, Observer { response: CountryListResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {

                    if (!response.message.isNullOrEmpty()) {
                        for (i in 0 until response.message.size) {
                            response.message[i].isClicked = false
                        }
                        msgList = response.message
                        /* msgList.add(Message("Nepal",false))
                         msgList.add(Message("Bhutan",false))*/
                        mAdapter = CountryListAdapter(activity, msgList, this@CountryListActivity)
                        recycleCountry.adapter = mAdapter
                        layoutManager = LinearLayoutManager(activity)
                        recycleCountry.layoutManager = layoutManager
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(this@CountryListActivity, recycleCountry, response.errorMessage.toString())

                } else {
                    activity.hideProgressBarNew()
                    activity.showMessage(this@CountryListActivity, recycleCountry, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(this@CountryListActivity, recycleCountry, response.errorMessage.toString())
            }
        })

    }


    override fun onBackPressed() {
        val intent = Intent(this@CountryListActivity, ActivityLocationFilter::class.java)
        intent.putExtra("FLAG", "1")
        /*intent.putExtra("PREV_SETTING",PREV_SETTING_FOR_AUTO);*/
        setResult(101, intent)
        finish()
    }
}