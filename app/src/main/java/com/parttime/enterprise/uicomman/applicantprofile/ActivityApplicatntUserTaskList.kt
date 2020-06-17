package com.parttime.enterprise.uicomman.applicantprofile

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.UserTaskHistoryListAdapter
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.UserTaskList
import kotlinx.android.synthetic.main.applicant_worker_history_details.*

class ActivityApplicatntUserTaskList : BaseActivity() {

    lateinit var adapter: UserTaskHistoryListAdapter
    var listHistory = ArrayList<UserTaskList>()
    lateinit var layoutManagers: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.applicant_worker_history_details)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            addJobBack.rotation = 180F
        } else {
            addJobBack.rotation = 0F
        }
        listHistory=intent.getSerializableExtra("UTASK") as ArrayList<UserTaskList>
        adapter = UserTaskHistoryListAdapter(activity, listHistory)
        workHistoryListDetailsRecycle.adapter = adapter
        layoutManagers= LinearLayoutManager(this@ActivityApplicatntUserTaskList)
        workHistoryListDetailsRecycle.layoutManager = layoutManagers

        addJobBack.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        finish()
    }

}