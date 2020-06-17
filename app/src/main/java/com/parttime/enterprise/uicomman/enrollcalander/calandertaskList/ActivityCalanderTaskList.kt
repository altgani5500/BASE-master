package com.parttime.enterprise.uicomman.enrollcalander.calandertaskList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cluqe.app.views.activities.home.ui.home.adapters.GroupProductHeaderHomeAdaptor
import com.parttime.enterprise.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollcalander.ActivityEnrollCalander
import com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels.Message
import com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels.ScheduleTimeResponse
import com.parttime.enterprise.viewmodels.CalenderTaskListViewModel
import kotlinx.android.synthetic.main.activity_enterprise_calender_task_list.*
import java.util.*

class ActivityCalanderTaskList : BaseActivity(), View.OnClickListener {

    private val listDatas = ArrayList<Message>()
    private lateinit var adaptors: GroupProductHeaderHomeAdaptor
    private lateinit var layoutManager: LinearLayoutManager
    lateinit var viewModel: CalenderTaskListViewModel
    private var dates="";
   // calRecycleView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterprise_calender_task_list)
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                ""
            ).equals("ar")
        ) {
            accountSettingbck.rotation = 180F
        } else {
            accountSettingbck.rotation = 0F
        }

        inItViews()

       if (Utilities.isNetworkAvailable(this@ActivityCalanderTaskList)) {
           showProgressBarNew()
           viewModel = ViewModelProviders.of(this@ActivityCalanderTaskList).get(CalenderTaskListViewModel::class.java)
           viewModel.getTaskThroughApi(
               appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
               dates,
               appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
               Utilities.isNetworkAvailable(this@ActivityCalanderTaskList)
           )
           apiResponserObserver()
       } else {
           showMessage(
               this@ActivityCalanderTaskList,
               accountSettingbck,
               getString(R.string.network_error)
           )
       }
   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1003){
            if (Utilities.isNetworkAvailable(this@ActivityCalanderTaskList)) {
                showProgressBarNew()
                viewModel = ViewModelProviders.of(this@ActivityCalanderTaskList).get(CalenderTaskListViewModel::class.java)
                viewModel.getTaskThroughApi(
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                    dates,
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                    Utilities.isNetworkAvailable(this@ActivityCalanderTaskList)
                )
                apiResponserObserver()
            } else {
                showMessage(
                    this@ActivityCalanderTaskList,
                    accountSettingbck,
                    getString(R.string.network_error)
                )
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun apiResponserObserver() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@ActivityCalanderTaskList, accountSettingbck, getString(msg))
        })
        viewModel.scheduleListViewModel.observe(this, Observer { response: ScheduleTimeResponse ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    if(listDatas.isNotEmpty()){
                        listDatas.clear()
                    }
                    if(response.message.isNotEmpty()) {
                        adaptors =
                            GroupProductHeaderHomeAdaptor(this@ActivityCalanderTaskList, listDatas)
                        calRecycleView.layoutManager =
                            LinearLayoutManager(
                                this@ActivityCalanderTaskList,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        calRecycleView.adapter = adaptors
                        listDatas.addAll(response.message)
                        adaptors.notifyDataSetChanged()
                    }else{
                        showToast(getString(R.string.there_are_empty_task))
                        onBackPressed()
                    }
                } else {
                    showMessage(
                        this@ActivityCalanderTaskList,
                        accountSettingbck,
                        response.error_message.toString()
                    )
                }
            } else {
                showMessage(this@ActivityCalanderTaskList, accountSettingbck, response.error_message.toString())
            }
        })
    }


    private fun inItViews() {
        accountSettingbck.setOnClickListener(this@ActivityCalanderTaskList)
        dates=intent.getStringExtra("DATE")
        if (intent.hasExtra("DATES")) {
            filterTitle.text = intent.getStringExtra("DATES")

        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.accountSettingbck -> {
                onBackPressed()
            }
        }
    }


    override fun onBackPressed() {
        val intent=Intent(this@ActivityCalanderTaskList, ActivityEnrollCalander::class.java)
        setResult(1001,intent)
        finish()
    }
}