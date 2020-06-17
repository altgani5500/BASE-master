package com.parttime.enterprise.uicomman.enrollworkerprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.TaskHistoryDayAdapter
import com.parttime.enterprise.adapters.WorkerProfileListAdapter
import com.parttime.enterprise.helpers.Utilities

import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.GenderModel
import com.parttime.enterprise.uicomman.applicantprofile.ApplicantProfileActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.CreateTaskActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollmores.EnrollMoreActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist.Task
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist.UserSchedules
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist.WorkerProfileListModelResponse
import com.parttime.enterprise.viewmodels.WorkerProfileListViewModel

import kotlinx.android.synthetic.main.activity_enroll_worker_profile.*
import kotlinx.android.synthetic.main.enroll_worker_profile_inflator_two.*

class EnrollWorkerProfileActivity : BaseActivity(), View.OnClickListener,TaskHistoryDayAdapter.OnItemClickDayHistory {


    // view model
    lateinit var viewModel: WorkerProfileListViewModel

    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: WorkerProfileListAdapter
    var taskLists = ArrayList<Task>()
    lateinit var workerProfileId:String
    lateinit var enrollUserId:String

    // button day
    lateinit var layoutManagerBtn: LinearLayoutManager
    lateinit var adapterBtn: TaskHistoryDayAdapter
    var btnList = ArrayList<UserSchedules>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enroll_worker_profile)
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                ""
            ).equals("ar")
        ) {
            enrollprofileBack.rotation = 180F
        } else {
            enrollprofileBack.rotation = 0F
        }
        enrollUserId=intent.getStringExtra("ENROLL_USER_ID")

        initView()
        userRole()
        callApiForProfileData(enrollUserId)
        initHistoryAdapter()
    }

    private fun initHistoryAdapter() {
        adapterBtn = TaskHistoryDayAdapter(activity, btnList, this@EnrollWorkerProfileActivity)
        taskHisRecycle.adapter = adapterBtn
        // layoutManager = LinearLayoutManager(activity)
        layoutManagerBtn = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        taskHisRecycle.layoutManager = layoutManagerBtn


    }

    override fun onClickDayHistory(values: ArrayList<UserSchedules>) {
        // var tempVar:UserSchedules
        for(i in 0 until values.size){
            if(values[i].isClicked){
                scheTxtStatus.text=values[i].status
                scheTxtPtime.text=values[i].punchTime
                scheTxtTime.text=values[i].schedule
                break
            }
        }

      // showToast(values.toString())
    }

    override fun onResume() {
        super.onResume()
        userRole()
        if(!taskLists.isNullOrEmpty()){
            taskLists.clear()
        }
        callApiForProfileData(enrollUserId)
    }

    private fun callApiForProfileData(stringExtra: String?) {
        viewModel = ViewModelProviders.of(this).get(WorkerProfileListViewModel::class.java)
        if (!stringExtra.isNullOrEmpty()) {
            showProgressBarNew()
            if (Utilities.isNetworkAvailable(this@EnrollWorkerProfileActivity)) {
                viewModel.getWorkerProfileList(
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
                    , stringExtra,
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                    Utilities.isNetworkAvailable(this@EnrollWorkerProfileActivity)

                )
                getWorkerProfileFromData()
            }
        } else {
            showMessage(
                this@EnrollWorkerProfileActivity,
                recycleWorkerProfileList,
                getString(R.string.network_error)
            )
        }

    }

    private fun userRole() {
        if (enterPriseAdminRole) {
            taskListLayout.visibility=View.VISIBLE
            fab_enroll.visibility=View.VISIBLE
            ll_empty_list_home.visibility=View.GONE
        } else if(appRecruiterRole) {
            taskListLayout.visibility=View.GONE
            fab_enroll.visibility=View.GONE
            ll_empty_list_home.visibility=View.VISIBLE
        }
        else if (appSchedulerRole || appSupervisorRole ) {
            taskListLayout.visibility=View.VISIBLE
            fab_enroll.visibility=View.VISIBLE
            ll_empty_list_home.visibility=View.GONE
        }else if(appSupervisorEvaluatorRole){
            taskListLayout.visibility=View.VISIBLE
            fab_enroll.visibility=View.GONE
            ll_empty_list_home.visibility=View.GONE
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            callApiForProfileData(intent.getStringExtra("ENROLL_USER_ID"))
        }
    }

    private fun initView() {
        enrollprofileBack.setOnClickListener(this@EnrollWorkerProfileActivity)
        txtMoreClick.setOnClickListener(this@EnrollWorkerProfileActivity)
        fab_enroll.setOnClickListener(this@EnrollWorkerProfileActivity)

        // initilise Adapter
        adapter = WorkerProfileListAdapter(activity, taskLists)
        recycleWorkerProfileList.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        //layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycleWorkerProfileList.layoutManager = layoutManager
    }

    override fun onClick(p0: View?) {
        when (p0) {
            enrollprofileBack -> {
                onBackPressed()
            }
            txtMoreClick -> {
                //launchActivity(EnrollMoreActivity::class.java)
                val intenti=Intent(this@EnrollWorkerProfileActivity,EnrollMoreActivity::class.java)
                intenti.putExtra("USERID",intent.getStringExtra("ENROLL_USER_ID"))
                startActivity(intenti)
            }
            fab_enroll -> {
                var intent=Intent(this@EnrollWorkerProfileActivity,CreateTaskActivity::class.java)
                intent.putExtra("WORKERID",workerProfileId)
                startActivityForResult(intent,101)
                //launchActivity(CreateTaskActivity::class.java)
            }

        }

    }


    private fun getWorkerProfileFromData() {
        hideProgressBarNew()
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@EnrollWorkerProfileActivity, recycleWorkerProfileList, getString(msg))
        })
        viewModel.workerProfileListViewModel.observe(
            this,
            Observer { response: WorkerProfileListModelResponse ->
                hideProgressBarNew()
                if (response.success) {
                    if (response.code == 200) {
                       // schedule task set
                        if(response.userSchedules.size>0){
                            response.userSchedules[0].isClicked=true
                        }
                        if(!btnList.isNullOrEmpty()){
                            btnList.clear()
                        }
                        btnList.addAll(response.userSchedules)
                        adapterBtn.notifyDataSetChanged()
                        if(response.workingHours.contentEquals("0")){
                            enroll_profile_hours.text="0 " +getString(R.string.hrss)
                        }else {
                            enroll_profile_hours.text = response.workingHours +" "+getString(R.string.hrss)
                        }

                        // task details and other stuff
                        txtProfileNationality.text = response.message.userDetail.jobTitle
                        workerProfileId=response.message.userDetail.userId.toString()
                        txtProfileName.text =
                            response.message.userDetail.firstName + " " + response.message.userDetail.lastName
                        emrollUserName.text =
                            response.message.userDetail.firstName + " " + response.message.userDetail.lastName
                        //
                        if (!response.message.userDetail.profilePicture.isNullOrEmpty())
                            Glide.with(this@EnrollWorkerProfileActivity).load(response.message.userDetail.profilePicture).into(imgUserPic_data)
                                //.asBitmap().override(300, 300)
                              //  .placeholder(R.drawable.b_scrren)
                               // .into(imgUserPic_data)
                        // set Adapter
                        if(!taskLists.isNullOrEmpty()){
                            taskLists.clear()
                        }
                        taskLists.addAll(response.message.taskList)
                        adapter.notifyDataSetChanged()
                        checkTaskList()

                        ll_profileDetails.setOnClickListener(View.OnClickListener {
                            val intent = Intent(activity, ApplicantProfileActivity::class.java)
                            intent.putExtra("ID", response.message.userDetail.userId.toString())
                            activity.startActivity(intent)
                        })

                    } else {
                        showMessage(
                            this@EnrollWorkerProfileActivity,
                            recycleWorkerProfileList,
                            response.errorMessage.toString()
                        )
                    }
                } else {
                    showMessage(
                        this@EnrollWorkerProfileActivity,
                        recycleWorkerProfileList,
                        response.errorMessage.toString()
                    )
                }
            })
    }

    override fun onBackPressed() {
        finish()
    }

    private fun checkTaskList(){
        if(!taskLists.isNullOrEmpty()){
            taskListLayout.visibility=View.VISIBLE
            ll_empty_list_home.visibility=View.GONE
            detailsView.visibility=View.GONE
        }else{
            taskListLayout.visibility=View.GONE
            ll_empty_list_home.visibility=View.VISIBLE
            detailsView.visibility=View.VISIBLE
        }
    }

}
