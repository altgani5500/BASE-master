package com.parttime.enterprise.uicomman.applicantprofile

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.WorkHistoryListAdapter
import com.parttime.enterprise.adapters.WorkerProfileListAdapter
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.WorkHistoryList
import kotlinx.android.synthetic.main.applicant_worker_history_details.*

class ActivityApplicatntWorHistoryDetails : BaseActivity() {

    lateinit var adapter: WorkHistoryListAdapter
    var listHistory = ArrayList<WorkHistoryList>()
    lateinit var layoutManagers: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.applicant_worker_history_details)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            addJobBack.rotation = 180F
        } else {
            addJobBack.rotation = 0F
        }
        listHistory=intent.getSerializableExtra("HISTORY") as ArrayList<WorkHistoryList>
        adapter = WorkHistoryListAdapter(activity, listHistory)
        workHistoryListDetailsRecycle.adapter = adapter
        layoutManagers= LinearLayoutManager(this@ActivityApplicatntWorHistoryDetails)
        workHistoryListDetailsRecycle.layoutManager = layoutManagers

        addJobBack.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        finish()
    }

}