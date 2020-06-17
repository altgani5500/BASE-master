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
import com.parttime.enterprise.adapters.StateListAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.helpers.Utilities.isNetworkAvailable
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.locationaddress.statemodel.Message
import com.parttime.enterprise.uicomman.locationaddress.statemodel.StateModelResponse
import com.parttime.enterprise.viewmodels.StateListViewModel
import kotlinx.android.synthetic.main.activity_country_layout.*

class StateActivity : BaseActivity(), StateListAdapter.ItemClickListener, View.OnClickListener {


    // lateinit var viewModel: CountryListViewModel
    private var countrySelet = " "
    private var msgList = ArrayList<com.parttime.enterprise.uicomman.locationaddress.statemodel.Message>()
    lateinit var mAdapter: StateListAdapter
    lateinit var layoutManager: LinearLayoutManager
    private var listSelectedItem = ArrayList<String>()
    lateinit var viewModel: StateListViewModel

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

   
    override fun onItemClicked(state: ArrayList<Message>) {
        if (!listSelectedItem.isNullOrEmpty()) {
            listSelectedItem.clear()
        }
        for (i in 0 until state.size) {
            if (state[i].isClicked) {
                listSelectedItem.add(state[i].state)
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

            val intent = Intent(this@StateActivity, ActivityLocationFilter::class.java)
            intent.putExtra("STATE", listSelectedItem)
            intent.putExtra("FLAG", "3")
            setResult(102, intent)
            finish()
        }
    }

    private fun inItView() {
        filterBack_locationa.setOnClickListener(this@StateActivity)
        jobAddTitle_txt3.setOnClickListener(this@StateActivity)
        cancel_Country.setOnClickListener(this@StateActivity)
    }

    /*
     Method to set recyclerview adapter
     */
    private fun setAdapter() {
        // initilise api
        if (!intent.getStringExtra("COUNTRY").isNullOrEmpty()) {
            if (Utilities.isNetworkAvailable(this@StateActivity)) {
                showProgressBarNew()
                viewModel = ViewModelProviders.of(activity).get(StateListViewModel::class.java)
                viewModel.getStateList(
                    activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                    activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                    intent.getStringExtra("COUNTRY"),
                    isNetworkAvailable(activity)
                )
                stateListResponse()
            } else {
                showMessage(this@StateActivity, recycleCountry, getString(R.string.network_error))
            }
        }

    }

    // state list get from api
    private fun stateListResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(this@StateActivity, recycleCountry, getString(msg))
        })
        viewModel.stateResponse.observe(this, Observer { response: StateModelResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {

                    if (!response.message.isNullOrEmpty()) {
                        for (i in 0 until response.message.size) {
                            response.message[i].isClicked = false
                        }
                        msgList = response.message
                        mAdapter = StateListAdapter(activity, msgList, this@StateActivity)
                        recycleCountry.adapter = mAdapter
                        layoutManager = LinearLayoutManager(activity)
                        recycleCountry.layoutManager = layoutManager
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(this@StateActivity, recycleCountry, response.errorMessage.toString())

                } else {
                    activity.hideProgressBarNew()
                    activity.showMessage(this@StateActivity, recycleCountry, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(this@StateActivity, recycleCountry, response.errorMessage.toString())
            }
        })

    }


    override fun onBackPressed() {
        val intent = Intent(this@StateActivity, ActivityLocationFilter::class.java)
        intent.putExtra("FLAG", "1")
        /*intent.putExtra("PREV_SETTING",PREV_SETTING_FOR_AUTO);*/
        setResult(101, intent)
        finish()
    }
}