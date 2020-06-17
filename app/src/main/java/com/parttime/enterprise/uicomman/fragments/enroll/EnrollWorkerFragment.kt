package com.parttime.enterprise.uicomman.fragments.enroll

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.ParrentEnrollAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseFragment
import com.parttime.enterprise.uicomman.enrollcalander.ActivityEnrollCalander

import com.parttime.enterprise.uicomman.fragments.enroll.workesmodels.SectionDataModel
import com.parttime.enterprise.uicomman.fragments.enroll.workesmodels.SingleItemModel
import com.parttime.enterprise.uicomman.fragments.enroll.workesmodels.response.EnrollWorkerListResponse
import com.parttime.enterprise.uicomman.notifications.NotificationListActivity
import com.parttime.enterprise.viewmodels.EnrollWorkerViewModel
import kotlinx.android.synthetic.main.fragment_enrolled_worker.*

class EnrollWorkerFragment : BaseFragment() {
    lateinit var layoutManager: LinearLayoutManager
    lateinit var viewModel: EnrollWorkerViewModel
    var sectionModel = ArrayList<SectionDataModel>()
    var enrollSearch: String = " "
    lateinit var adapter: ParrentEnrollAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_enrolled_worker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this@EnrollWorkerFragment).get(EnrollWorkerViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setAdapter()

    }

    private fun setOnClickListener() {
        enroll_Search_Titile.visibility = View.VISIBLE
        jobSearch.setOnClickListener {
            enroll_Search_Titile.visibility = View.GONE
            jobSearch.visibility=View.GONE
            ll_inputSearch.visibility = View.VISIBLE
        }

        cancel_SearchbgMsg.setOnClickListener {
            enroll_Search_Titile.visibility = View.VISIBLE
            ll_inputSearch.visibility = View.GONE
            jobSearch.visibility=View.VISIBLE
            input_messageSearch.setText(" ")
            input_messageSearch.setHint(R.string.search)
        }

        calander_noti.setOnClickListener {
            if(geAccountType()==3){
                if(!sectionModel.isNullOrEmpty()){
                    activity.launchActivity(ActivityEnrollCalander::class.java)
                }else{
                    activity.showMessage(activity,calander_noti,getString(R.string.err_enroll_empty))
                }
            }else if(geAccountType()==2){
                if(!sectionModel.isNullOrEmpty()){
                    activity.launchActivity(ActivityEnrollCalander::class.java)
                }else{
                    activity.showMessage(activity,calander_noti,getString(R.string.err_enroll_empty))
                }
            }else {
                if(!sectionModel.isNullOrEmpty()){
                    activity.showMessage(activity,calander_noti,getString(R.string.err_account_type))
                }else{
                    activity.showMessage(activity,calander_noti,getString(R.string.err_enroll_empty))
                }

            }

        }

        // edit search functionality
        input_messageSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //toast(message = "Number is $s")
                //mAdapter!!.filter.filter(s)
                if (!s.toString().isNullOrEmpty()) {
                    var searchValue = s.toString()
                    if (searchValue.length > 0) {
                        apiCallForEnrollListSearch(searchValue)
                    }
                } else {
                    apiCallForEnrollList()
                }
            }

        })

        // notificationClickListner
        jobNotify.setOnClickListener {
            activity.launchActivity(NotificationListActivity::class.java)
        }

    }

    override fun onResume() {
        super.onResume()
        if (!ll_inputSearch.isVisible) {
            apiCallForEnrollList()
        }
    }

    // api call
    private fun apiCallForEnrollList() {
        if (Utilities.isNetworkAvailable(context)) {
            activity.showProgressBarNew()
            viewModel.getEnrollWorkerListApi(
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()), " "
                , activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                Utilities.isNetworkAvailable(context)
            )
            apiEnrollListResponse()
        } else {
            activity.showMessage(context, top_ll, getString(R.string.network_error))
        }
    }

    // api call Search
    private fun apiCallForEnrollListSearch(searchKey: String) {
        if (Utilities.isNetworkAvailable(context)) {
            activity.showProgressBarNew()
            viewModel.getEnrollWorkerListApi(
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()), searchKey
                , activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                Utilities.isNetworkAvailable(context)
            )
            apiEnrollListResponse()
        } else {
            activity.showMessage(context, top_ll, getString(R.string.network_error))
        }
    }

    // check empty or not
    private fun checkJobList() {
        if (sectionModel.isNullOrEmpty()) {
            ll_empty_list_home_enroll.visibility = View.VISIBLE
            rv_main.visibility = View.GONE
        } else {
            rv_main.visibility = View.VISIBLE
            ll_empty_list_home_enroll.visibility = View.GONE
        }
    }

    private fun setAdapter() {
        // add dummy Workers
        /*    var sectionModel = ArrayList<SectionDataModel>()
            for (i in 0..5) {
                var section = SectionDataModel()
                section.headerTitle = "Branch $i"
                var singleItem = ArrayList<SingleItemModel>()
                for (j in 0..2) {
                    singleItem.add(
                        SingleItemModel(
                            "Android Develeper", j.toString(),
                            "data:image/png;base64,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",
                            "$j Years of Experience", "$j Jun", ""
                        )
                    )
                }
                section.allItemsInSection = singleItem
                sectionModel.add(section)
            }*/
        rv_main.setHasFixedSize(true)
        adapter = ParrentEnrollAdapter(activity, sectionModel)
        rv_main.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        rv_main.layoutManager = layoutManager

    }

    // get enroll Worker list from api
    private fun apiEnrollListResponse() {
        activity.hideProgressBarNew()
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, top_ll, getString(msg))
        })
        viewModel.enrollWorkerListViewModel.observe(this, Observer { response: EnrollWorkerListResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {

                    // here we need To Call Adapter
                    if (response.success) {

                        //
                        if (sectionModel.isNotEmpty()) {
                            sectionModel.clear()
                        }
                        if (!response.message.isNullOrEmpty()) {
                            for (i in 0 until response.message.size) {
                                var section = SectionDataModel()
                                section.headerTitle = response.message[i].branchName
                                var singleItem = ArrayList<SingleItemModel>()
                                if (!response.message[i].enrolledUsers.isNullOrEmpty()) {
                                    for (j in 0 until response.message[i].enrolledUsers.size) {
                                        singleItem.add(
                                            SingleItemModel(
                                                response.message[i].enrolledUsers[j].name,
                                                response.message[i].enrolledUsers[j].userId.toString(),
                                                response.message[i].enrolledUsers[j].profilePicture,
                                                response.message[i].enrolledUsers[j].jobTitle,
                                                response.message[i].enrolledUsers[j].enrolledAt,
                                                "",
                                                response.message[i].enrolledUsers[j].workNetwork
                                            )
                                        )
                                    }
                                    section.allItemsInSection = singleItem
                                    sectionModel.add(section)
                                }

                            }
                        }
                        checkJobList()
                        adapter.notifyDataSetChanged()
                    } else {
                        activity.hideProgressBarNew()
                        activity.showMessage(context, top_ll, response.errorMessage.toString())
                        checkJobList()
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, top_ll, response.errorMessage.toString())
                    checkJobList()
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, top_ll, response.errorMessage.toString())
                    checkJobList()
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(context, top_ll, response.errorMessage.toString())
                checkJobList()

            }
        })

    }
}