package com.parttime.enterprise.uicomman.notifications

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.NotificationListAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.notifications.notificationmodels.Message
import com.parttime.enterprise.uicomman.notifications.notificationmodels.NotificationResponseModels
import com.parttime.enterprise.viewmodels.NotificationListViewModel
import kotlinx.android.synthetic.main.activity_notification_list.*

class NotificationListActivity : BaseActivity(), View.OnClickListener {


    lateinit var viewModel: NotificationListViewModel
    var sectionModel = ArrayList<Message>()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter1: NotificationListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            profile_backedt.rotation = 180F
        } else {
            profile_backedt.rotation = 0F
        }
        initView()
        setAdapter()
    }

    private fun initView() {
        profile_backedt.setOnClickListener(this@NotificationListActivity)
    }


    override fun onClick(p0: View?) {
        when (p0) {
            profile_backedt -> {
                onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Utilities.isNetworkAvailable(this@NotificationListActivity)) {
            showProgressBarNew()
            viewModel = ViewModelProviders.of(activity).get(NotificationListViewModel::class.java)
            viewModel.getNotificationList(
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                Utilities.isNetworkAvailable(this@NotificationListActivity)
            )
            apiNotificationListResponse()
        } else {
            showMessage(this@NotificationListActivity, recycleView_NotiList, getString(R.string.network_error))
        }
    }

    // get notification list from api
    private fun apiNotificationListResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@NotificationListActivity, recycleView_NotiList, getString(msg))
        })
        viewModel.notificationListViewModel.observe(this, Observer { response: NotificationResponseModels ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {

                    // here we need To Call Adapter
                    if (response.success) {

                        // add dummy Workers
                        if (sectionModel.isNotEmpty()) {
                            sectionModel.clear()
                        }
                        sectionModel.addAll(response.message)
                        adapter1.notifyDataSetChanged()
                        checkJobList()
                    } else {
                        hideProgressBarNew()
                        showMessage(
                            this@NotificationListActivity,
                            recycleView_NotiList,
                            response.errorMessage.toString()
                        )
                        checkJobList()
                    }

                } else if (response.code == 204) {
                    hideProgressBarNew()
                    showMessage(this@NotificationListActivity, recycleView_NotiList, response.errorMessage.toString())
                    checkJobList()
                } else if (response.code == 401) {
                    hideProgressBarNew()
                    showMessage(this@NotificationListActivity, recycleView_NotiList, response.errorMessage.toString())
                    checkJobList()
                }
            } else {
                hideProgressBarNew()
                showMessage(this@NotificationListActivity, recycleView_NotiList, response.errorMessage.toString())
                checkJobList()

            }
        })

    }

    // notification List Adapter
    private fun setAdapter() {
        recycleView_NotiList.setHasFixedSize(true)
        adapter1 = NotificationListAdapter(activity, sectionModel)
        recycleView_NotiList.adapter = adapter1
        layoutManager = LinearLayoutManager(activity)
        recycleView_NotiList.layoutManager = layoutManager
        recycleView_NotiList.visibility = View.VISIBLE
    }

    // check empty or not
    private fun checkJobList() {
        if (sectionModel.isNullOrEmpty()) {
            ll_empty_list.visibility = View.VISIBLE
            recycleView_NotiList.visibility = View.GONE
        } else {
            recycleView_NotiList.visibility = View.VISIBLE
            ll_empty_list.visibility = View.GONE
        }
    }


    override fun onBackPressed() {
        finish()
    }
}