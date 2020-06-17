package com.parttime.enterprise.uicomman.subuser

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.GetSubUserListAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.subuser.adduserlist.GetSubUserListResponse
import com.parttime.enterprise.uicomman.subuser.adduserlist.Message
import com.parttime.enterprise.viewmodels.AddSubUserViewModel
import kotlinx.android.synthetic.main.activity_subuser.*

class AddSubUserActivity : BaseActivity(), View.OnClickListener {
    // adapter Initialise
    lateinit var adapter: GetSubUserListAdapter
    var listUserList = ArrayList<Message>()
    lateinit var layoutManager: LinearLayoutManager
    // view model
    lateinit var viewModel: AddSubUserViewModel

    //onclick
    override fun onClick(v: View?) {
        when (v) {
            accountSettingbck_add_user -> {
                onBackPressed()
            }
            btn_createUser -> {
                launchActivity(CreateSubUserActivity::class.java)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subuser)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountSettingbck_add_user.rotation = 180F
        } else {
            accountSettingbck_add_user.rotation = 0F
        }
        initClickListner()
        setAdapterView()
    }

    private fun setAdapterView() {
        adapter = GetSubUserListAdapter(activity, listUserList)
        rclview_add_subUser.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        rclview_add_subUser.layoutManager = layoutManager
    }

    private fun initClickListner() {
        accountSettingbck_add_user.setOnClickListener(this@AddSubUserActivity)
        btn_createUser.setOnClickListener(this@AddSubUserActivity)
    }

    override fun onResume() {
        super.onResume()
        if (Utilities.isNetworkAvailable(this@AddSubUserActivity)) {
            apiApplicantProfileResponse()
        } else {
            activity.showMessage(activity, accountSettingbck_add_user, getString(R.string.network_error))
            checkJobList()
        }
    }


    // get api response for sub user list
    private fun apiApplicantProfileResponse() {
        activity.showProgressBarNew()
        viewModel = ViewModelProviders.of(this@AddSubUserActivity).get(AddSubUserViewModel::class.java)

        viewModel.getSubUserList(
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
            ,
            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
            Utilities.isNetworkAvailable(activity)
        )
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(activity, accountSettingbck_add_user, getString(msg))
        })
        viewModel.subUserGetViewModelRes.observe(this, Observer { response: GetSubUserListResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        if (!listUserList.isNullOrEmpty()) {
                            listUserList.clear()
                        }
                        listUserList.addAll(response.message)
                        adapter.notifyDataSetChanged()
                        checkJobList()
                    } else {
                        activity.showMessage(activity, accountSettingbck_add_user, response.errorMessage.toString())
                        checkJobList()
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    checkJobList()
                    activity.showMessage(activity, accountSettingbck_add_user, response.errorMessage.toString())
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    checkJobList()
                    activity.showMessage(activity, accountSettingbck_add_user, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                checkJobList()
                activity.showMessage(activity, accountSettingbck_add_user, response.errorMessage.toString())

            }
        })
    }

    private fun checkJobList() {
        if (listUserList.isNullOrEmpty()) {
            ll_empty_list_subuser.visibility = View.VISIBLE
            rclview_add_subUser.visibility = View.GONE
        } else {
            rclview_add_subUser.visibility = View.VISIBLE
            ll_empty_list_subuser.visibility = View.GONE
        }
    }


    override fun onBackPressed() {
        finish()
    }
}