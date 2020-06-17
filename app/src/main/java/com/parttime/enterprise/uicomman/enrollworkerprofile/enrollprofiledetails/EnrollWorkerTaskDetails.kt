package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.EditTaskActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.detail.TaskDetailResponseModel
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.dialogevolution.DialogEvolutionModelRes
import com.parttime.enterprise.uicomman.homes.HomeActivity
import com.parttime.enterprise.viewmodels.ErollWorkertaskDetailViewModel
import kotlinx.android.synthetic.main.enroll_worker_profile_details.*

class EnrollWorkerTaskDetails : BaseActivity(), View.OnClickListener {


    // view model
    lateinit var viewModel: ErollWorkertaskDetailViewModel
    lateinit var taskIds: String
    var buttonType: Int = 0
    private var taskType = 0
    private var taskName = " "
    private var taskdesc = " "
    private var dates = " "
    private var startTime = " "
    private var endTime = " "
    private var superEvaluatorId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enroll_worker_profile_details)

        taskIds = intent.getStringExtra("ID")
        initViews()


    }

    private fun initViews() {
        enrollprofileBack.setOnClickListener(this@EnrollWorkerTaskDetails)
        taskTypeButton.setOnClickListener(this@EnrollWorkerTaskDetails)
        jobAddTitle_txt.setOnClickListener(this@EnrollWorkerTaskDetails)
        // check and Api Hit
        if (Utilities.isNetworkAvailable(this@EnrollWorkerTaskDetails)) {
            showProgressBarNew()
            viewModel = ViewModelProviders.of(this).get(ErollWorkertaskDetailViewModel::class.java)
            viewModel.taskProfileDetails(
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                taskIds.toString(),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                Utilities.isNetworkAvailable(this@EnrollWorkerTaskDetails)
            )
            getWorkerProfileFromData()
        } else {
            showMessage(
                this@EnrollWorkerTaskDetails,
                emrollUserName,
                getString(R.string.network_error)
            )
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            enrollprofileBack -> {
                onBackPressed()
            }
            taskTypeButton -> {
                if (buttonType == 1) {
                    showDialogEvaluation("evaluate")
                } else if (buttonType == 2) {
                    showDialogEvaluation("reevaluate")
                }
            }
            jobAddTitle_txt -> {
                val intent = Intent(this@EnrollWorkerTaskDetails, EditTaskActivity::class.java)
                intent.putExtra("TTYPE", taskType)
                intent.putExtra("TID", taskIds)
                intent.putExtra("TNAME", taskName)
                intent.putExtra("TDESC", taskdesc)
                intent.putExtra("TDATE", dates)
                intent.putExtra("TSTIME", startTime)
                intent.putExtra("TETIME", endTime)
                intent.putExtra("TSE", superEvaluatorId)
                startActivity(intent)
                finish()
            }


        }
    }

    /*Custom Dialog Evaluation*/
    fun showDialogEvaluation(type: String) {
        val dialog = Dialog(activity)
        this.window
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_rating_evaluation)
        val rate = dialog.findViewById(R.id.spnrRates) as Spinner
        val reasion = dialog.findViewById(R.id.edtResionDialog) as EditText
        val yesBtn = dialog.findViewById(R.id.btnSubmitTasks) as Button
        yesBtn.setOnClickListener {
            if (Utilities.isNetworkAvailable(this@EnrollWorkerTaskDetails)) {
                if (reasion.text.toString().length > 2) {
                    var map = HashMap<String, String>()
                    map.put("taskId", taskIds)
                    map.put("status", type)
                    map.put("rating", rate.selectedItem.toString())
                    map.put("reason", reasion.text.toString())
                    showProgressBarNew()
                    viewModel.taskProfileDetailsDialogEvolution(
                        appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                        map,
                        appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                        Utilities.isNetworkAvailable(this@EnrollWorkerTaskDetails)
                    )
                    getWorkerProfileFromDataDialog()
                    dialog.dismiss()
                } else {
                    showMessage(
                        this@EnrollWorkerTaskDetails,
                        enrollprofileBack,
                        getString(R.string.err_resion)
                    )
                }
            } else {
                showToast(getString(R.string.network_error))
            }

        }

        dialog.show()
    }

    /*Custom Dialog For  Feedback*/
    fun showDialogFeedBack() {
        val dialog = Dialog(activity)
        this.window
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_thanks_feedback)

        val yesBtn = dialog.findViewById(R.id.feedbackLayout) as LinearLayout
        yesBtn.setOnClickListener {
            onBackPressed()
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun getWorkerProfileFromDataDialog() {
        hideProgressBarNew()
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@EnrollWorkerTaskDetails, enrollprofileBack, getString(msg))
        })
        viewModel.workerProfileDetailsDilog.observe(
            this,
            Observer { response: DialogEvolutionModelRes ->
                hideProgressBarNew()
                if (response.success) {
                    if (response.code == 200) {
                        // showToast(response.message)
                        showDialogFeedBack()
                    } else {
                        showMessage(
                            this@EnrollWorkerTaskDetails,
                            enrollprofileBack,
                            response.errorMessage.toString()
                        )
                    }
                } else {
                    showMessage(
                        this@EnrollWorkerTaskDetails,
                        enrollprofileBack,
                        response.errorMessage.toString()
                    )
                }
            })
    }


    private fun getWorkerProfileFromData() {
        hideProgressBarNew()
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@EnrollWorkerTaskDetails, enrollprofileBack, getString(msg))
        })
        viewModel.workerProfileDetails.observe(
            this,
            Observer { response: TaskDetailResponseModel ->
                hideProgressBarNew()
                if (response.success) {
                    if (response.code == 200) {
                        // dummy data set
                        taskType = response.message.taskDetail.taskType
                        taskName = response.message.taskDetail.taskName
                        taskdesc = response.message.taskDetail.taskDescription
                        if (response.message.taskDetail.taskDate.isNullOrEmpty()) {
                            dates = " "
                            startTime = " "
                            endTime = " "
                        } else {
                            dates = response.message.taskDetail.taskDate
                            startTime = response.message.taskDetail.startTime
                            endTime = response.message.taskDetail.endTime
                        }
                        superEvaluatorId = response.message.taskDetail.evaluators
                        if (!response.message.userDetail.profilePicture.isNullOrEmpty())
                            Glide.with(this@EnrollWorkerTaskDetails).load(response.message.userDetail.profilePicture)/*.asBitmap().override(
                                300,
                                300
                            )
                                .placeholder(R.drawable.b_scrren)*/.into(imgUserPic_data)
                        txtProfileName.text =
                            response.message.userDetail.firstName + " " + response.message.userDetail.lastName
                        emrollUserName.text =
                            response.message.userDetail.firstName + " " + response.message.userDetail.lastName
                        //intent.getStringExtra("USERNAME")
                        txtProfileNationality.text = response.message.userDetail.jobTitle
                        taskDetailsAppDesign.text = response.message.taskDetail.taskName
                        if (response.message.taskDetail.taskType == 1) {
                            taskDetailsTaskType.text = getString(R.string.continuous)
                        } else if (response.message.taskDetail.taskType == 2) {
                            taskDetailsTaskType.text = getString(R.string.timebound)
                        }
                        taskDetailsDesc.text = response.message.taskDetail.taskDescription
                        if (!response.message.taskDetail.taskDate.isNullOrEmpty() && !response.message.taskDetail.startTime.isNullOrEmpty() && !response.message.taskDetail.endTime.isNullOrEmpty()) {
                            var sTime = response.message.taskDetail.startTime.split(":")
                            var eTime = response.message.taskDetail.endTime.split(":")
                            // for start time
                            var sString = " "
                            if (sTime[0].toInt() < 13) {
                                sString = getString(R.string.ams)
                            } else {
                                sString = getString(R.string.pms)
                            }

                            var eString = " "
                            if (eTime[0].toInt() < 13) {
                                eString = getString(R.string.ams)
                            } else {
                                eString = getString(R.string.pms)
                            }
                             var sMin=" "
                            if(sTime[1].length<2 && sTime[1].toInt()<10){
                                sMin="0"+sTime[1]
                            }else{
                                sMin=sTime[1]
                            }

                            var eMin=" "
                            if(eTime[1].length<2 && eTime[1].toInt()<10){
                                eMin="0"+eTime[1]
                            }else{
                                eMin=eTime[1]
                            }
                            var sHour=" "
                            if(sTime[0].length<2 && sTime[0].toInt()<10){
                                sHour="0"+sTime[0]
                            }else{
                                sHour=sTime[0]
                            }
                            var eHour=" "
                            if(eTime[0].length<2 && eTime[0].toInt()<10 ){
                                eHour="0"+eTime[0]
                            }else{
                                eHour=eTime[0]
                            }
                            txtTaskDetailsDateandTime.text =
                                response.message.taskDetail.taskDate + "  " + getWorkerListDate(sHour+":"+sMin + sString )+ " " + getString(
                                    R.string.space_end_time
                                ) + " " +getWorkerListDate( eHour +":"+eMin+ eString)
                            /*if (sTime[0].toInt() in 1..9 ||
                                eTime[0].toInt() in 1..9
                            ) {
                                txtTaskDetailsDateandTime.text =
                                    response.message.taskDetail.taskDate + "  0" + response.message.taskDetail.startTime + sString + " " + getString(
                                        R.string.space_end_time
                                    ) + " 0" + response.message.taskDetail.endTime + eString
                            } else {
                                txtTaskDetailsDateandTime.text =
                                    response.message.taskDetail.taskDate + "  " + response.message.taskDetail.startTime + sString + " " + getString(
                                        R.string.space_end_time
                                    ) + " " + response.message.taskDetail.endTime + eString
                            }*/

                            txtDateandTimeHeader.visibility = View.VISIBLE
                            txtTaskDetailsDateandTime.visibility = View.VISIBLE
                        } else {
                            txtTaskDetailsDateandTime.text = " "
                            txtDateandTimeHeader.visibility = View.GONE
                            txtTaskDetailsDateandTime.visibility = View.GONE
                        }
                        if ("Pending".contentEquals(response.message.taskDetail.taskStatus)) {
                            viewsStatus.setBackgroundResource(R.drawable.circle_pending)
                            viewsStatusText.text = response.message.taskDetail.taskStatus
                            viewsStatusText.setTextColor(getColor(R.color.color_pending))
                            jobAddTitle_txt.visibility = View.GONE
                        } else if ("Completed".contentEquals(response.message.taskDetail.taskStatus)) {

                            jobAddTitle_txt.visibility = View.GONE
                            // if task status is completed then check which type of Button is visible
                            // this  logic is based on backend logic which is given by rishav
                            if (response.message.taskDetail.evaluation == 0) {

                                viewsStatus.setBackgroundResource(R.drawable.orange_circle)
                                viewsStatusText.text =
                                    resources.getString(R.string.pending_evaluation)
                                viewsStatusText.setTextColor(getColor(R.color.orange))

                                taskTypeButton.visibility = View.VISIBLE
                                taskTypeButton.text = resources.getString(R.string.evaluation)
                                buttonType = 1

                            } else {

                                viewsStatus.setBackgroundResource(R.drawable.circle_completed)
                                viewsStatusText.text = response.message.taskDetail.taskStatus
                                viewsStatusText.setTextColor(getColor(R.color.color_completed))
                                taskTypeButton.visibility = View.GONE
                                buttonType = 0

                            }
                        } else if ("In Process".contentEquals(response.message.taskDetail.taskStatus)) {
                            viewsStatus.setBackgroundResource(R.drawable.circle_green)
                            viewsStatusText.text = response.message.taskDetail.taskStatus
                            viewsStatusText.setTextColor(getColor(R.color.profile_green))
                            jobAddTitle_txt.visibility = View.GONE
                        } else if ("Rescheduled".contentEquals(response.message.taskDetail.taskStatus)) {
                            viewsStatus.setBackgroundResource(R.drawable.circle_revaluation)
                            viewsStatusText.text = response.message.taskDetail.taskStatus
                            viewsStatusText.setTextColor(getColor(R.color.color_revolution))
                            // check user base role hide show
                            if (appSchedulerRole || appSupervisorRole) {
                                jobAddTitle_txt.visibility = View.VISIBLE
                            } else if (appSupervisorEvaluatorRole) {
                                jobAddTitle_txt.visibility = View.GONE
                            }

                        } else if ("ReEvaluation".contentEquals(response.message.taskDetail.taskStatus)) {
                            viewsStatus.setBackgroundResource(R.drawable.circle_revaluation)
                            viewsStatusText.text = response.message.taskDetail.taskStatus
                            viewsStatusText.setTextColor(getColor(R.color.color_revolution))
                            jobAddTitle_txt.visibility = View.GONE
                            // change senerion basis if revolution status is exist the sho this button
                            if (response.message.taskDetail.reevaluation == 0 && "ReEvaluation".contentEquals(
                                    response.message.taskDetail.taskStatus
                                )
                            ) {
                                taskTypeButton.visibility = View.VISIBLE
                                taskTypeButton.text = resources.getString(R.string.reevaluates)
                                buttonType = 2

                            } else {
                                taskTypeButton.visibility = View.GONE
                                //  taskTypeButton.text = getString(R.string.reevaluates)
                                buttonType = 0
                            }
                        }
                        // check user base role hide show
                        if (appSchedulerRole || appSupervisorRole) {
                            if (buttonType == 1) {
                                taskTypeButton.visibility = View.VISIBLE
                            } else if (buttonType == 2) {
                                taskTypeButton.visibility = View.GONE
                            }
                        } else if (appSupervisorEvaluatorRole) {
                            if (buttonType == 1) {
                                taskTypeButton.visibility = View.GONE
                            } else if (buttonType == 2) {
                                taskTypeButton.visibility = View.VISIBLE
                            }
                        }
                        if (!response.message.taskDetail.evaluatorName.isNullOrEmpty()) {
                            txtschedulertaskName.visibility = View.GONE
                            txtschedulertask.visibility = View.GONE
                        } else {
                            txtschedulertaskName.visibility = View.VISIBLE
                            txtschedulertask.visibility = View.VISIBLE
                            txtschedulertaskName.text = response.message.taskDetail.evaluatorName
                        }
                        if (response.message.taskDetail.taskRating.isNotEmpty() && response.message.taskDetail.taskRating.length > 1) {
                            txttaskRatingName.visibility = View.VISIBLE
                            txttaskRating.visibility = View.VISIBLE
                            txttaskRatingName.text = response.message.taskDetail.taskRating
                        } else {
                            txttaskRatingName.visibility = View.GONE
                            txttaskRating.visibility = View.GONE
                        }
                    } else {
                        showMessage(
                            this@EnrollWorkerTaskDetails,
                            enrollprofileBack,
                            response.errorMessage.toString()
                        )
                    }
                } else {
                    showMessage(
                        this@EnrollWorkerTaskDetails,
                        enrollprofileBack,
                        response.errorMessage.toString()
                    )
                }
            })
    }


    override fun onBackPressed() {
        if (intent.hasExtra("FLAGS_BACK")) {
            if (intent.getIntExtra("FLAGS_BACK", 0) == 1) {
                launchActivityMain(HomeActivity::class.java)
                finish()
            } else {
                finish()
            }
        } else
            finish()
    }
}