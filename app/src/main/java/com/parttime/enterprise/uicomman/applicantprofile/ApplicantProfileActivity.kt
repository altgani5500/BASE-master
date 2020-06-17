package com.parttime.enterprise.uicomman.applicantprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.DocumentProfileAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.ApplicantProfileModelResponse
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.UserDocument
import com.parttime.enterprise.uicomman.pinchzoom.PinchZoomActivity
import com.parttime.enterprise.viewmodels.ApplicantProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ApplicantProfileActivity : BaseActivity() {

    lateinit var viewModel: ApplicantProfileViewModel
    var lat: String = "0.0"
    var lon: String = "0.0"
    lateinit var adapter: DocumentProfileAdapter
    var listDocument = ArrayList<UserDocument>()
    lateinit var layoutManagerAge: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            imgProfileBack.rotation = 180F
        } else {
            imgProfileBack.rotation = 0F
        }
        // onBack
        imgProfileBack.setOnClickListener {
            onBackPressed()
        }
        if (Utilities.isNetworkAvailable(activity)) {
            apiApplicantProfileResponse(intent.getStringExtra("ID"))
        } else {
            showMessage(activity, imgProfileBack, getString(R.string.network_error))
        }

    }

    /*Get ApplicantProfileData Type From Api*/
    private fun apiApplicantProfileResponse(id: String) {
        activity.showProgressBarNew()
        viewModel = ViewModelProviders.of(this@ApplicantProfileActivity).get(ApplicantProfileViewModel::class.java)

        viewModel.getApplicantProfilApi(
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
            ,
            id,
            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
            Utilities.isNetworkAvailable(activity)
        )
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(activity, imgProfileBack, getString(msg))
        })
        viewModel.applicantResponseModel.observe(this, Observer { response: ApplicantProfileModelResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter

                    if (response.success) {
                        userName_userProfile.text = response.message.name
                        txtProfileName.text = response.message.name
                        txtProfileGender.text = response.message.gender
                        Log.e("Applicant_Profile", response.message.toString())
                        if (response.message.age.isNullOrEmpty()) {
                            txtProAge.text = "--"
                        } else {
                            txtProAge.text = response.message.age
                        }
                        txtProfileNationality.text = response.message.nation
                        txtProfileEmail.text = response.message.email
                        if (response.message.education.isEmpty()) {
                            txtProfileCompanyName.text = "--"
                        } else {
                            txtProfileCompanyName.text = response.message.education
                        }
                        if (response.message.educationDetail.isNullOrEmpty()) {
                            details_ll.visibility = View.GONE
                            txtProfileCompanyDetails.text = response.message.educationDetail
                        } else {
                            details_ll.visibility = View.VISIBLE
                            txtProfileCompanyDetails.text = response.message.educationDetail
                        }
                        // }
                        txtLocationProfile.text = response.message.address
                        if (response.message.userDocuments.isNullOrEmpty()) {
                            txtProfileCv_RecycleView.visibility = View.GONE
                        } else {
                            txtProfileCv_RecycleView.visibility = View.VISIBLE
                            adapter = DocumentProfileAdapter(activity, response.message.userDocuments)
                            txtProfileCv_RecycleView.adapter = adapter
                            layoutManagerAge = LinearLayoutManager(this@ApplicantProfileActivity)
                            txtProfileCv_RecycleView.layoutManager = layoutManagerAge
                        }
                        allTask_txt.text = response.message.workHistory.allTask.toString()
                        pending_profile.text = response.message.workHistory.pending.toString()
                        pending_inProcess.text = response.message.workHistory.inprocess.toString()
                        profile_applicant_total.text = response.message.workHistory.workDone.toString()
                        complete_txt_count.text = response.message.workHistory.completed.toString()
                        profile_total_work_history.text = response.message.workHistory.userEnroll.toString()
                        if (response.message.profilePicture.isNullOrEmpty()) {
                            Picasso.get().load("no image").placeholder(R.drawable.others)
                                .into(imgUserPic_data)
                        } else {
                            Picasso.get().load(response.message.profilePicture).placeholder(R.drawable.others)
                                .into(imgUserPic_data)
                        }
                        // IMGURL
                        imgUserPic_data.setOnClickListener {
                            val intent = Intent(this@ApplicantProfileActivity, PinchZoomActivity::class.java)
                            intent.putExtra("IMGURL", response.message.profilePicture)
                            startActivity(intent)
                        }
                        ll_mapshow.setOnClickListener {
                            val strUri =
                                "http://maps.google.com/maps?q=loc:" + response.message.userLat + "," + response.message.userLong + " (" + "Part Time Enterprise" + ")"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
                            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
                            startActivity(intent)
                        }
                     //   if(response.message.userSeting.complete_profile==0){
                            if(response.message.userSeting.gender==1){
                                txtProfileGender.visibility=View.VISIBLE
                                txtProfileGenderView.visibility=View.VISIBLE
                            }else{
                                txtProfileGender.visibility=View.GONE
                                txtProfileGenderView.visibility=View.GONE
                            }
                            if(response.message.userSeting.age==1){
                                txtProAge.visibility=View.VISIBLE
                                txtProAgeView.visibility=View.VISIBLE
                            }else{
                                txtProAge.visibility=View.GONE
                                txtProAgeView.visibility=View.GONE
                            }
                            if(response.message.userSeting.education_level==1){
                                educationLayout.visibility=View.VISIBLE
                                // details_ll.visibility=View.VISIBLE
                                viesOne.visibility=View.VISIBLE
                            }else{
                                educationLayout.visibility=View.GONE
                                // details_ll.visibility=View.GONE
                                viesOne.visibility=View.GONE
                            }
                            if(response.message.userSeting.locations==1){
                                locationIdone.visibility=View.VISIBLE
                                lView.visibility=View.VISIBLE
                            }else{
                                locationIdone.visibility=View.GONE
                                lView.visibility=View.GONE
                            }
                            if(response.message.userSeting.cv==1){
                                layCertificate.visibility=View.VISIBLE
                            }else{
                                layCertificate.visibility=View.GONE
                            }
                            if(response.message.userSeting.work_hours==1){
                                isWorkingViews.visibility = View.VISIBLE
                                profile_applicant_total.visibility = View.VISIBLE
                            }else {
                                if (response.message.userSeting.isApplicant == 1) {
                                    isWorkingViews.visibility = View.VISIBLE
                                    profile_applicant_total.visibility = View.VISIBLE
                                } else {
                                    isWorkingViews.visibility = View.GONE
                                    profile_applicant_total.visibility = View.GONE
                                }
                            }

                        // task history click listenr
                        if(response.message.workHistoryList.size>0) {
                            taskHistoryCardView.setOnClickListener {
                                val intents = Intent(
                                    this@ApplicantProfileActivity,
                                    ActivityApplicatntWorHistoryDetails::class.java
                                )
                                intents.putExtra("HISTORY", response.message.workHistoryList)
                                startActivity(intents)
                            }
                        }

                        if(response.message.isUserTaskShow==1 && response.message.userTaskList.size>0){
                            taskCardView.setOnClickListener {
                                val intents = Intent(
                                    this@ApplicantProfileActivity,
                                    ActivityApplicatntUserTaskList::class.java
                                )
                                intents.putExtra("UTASK", response.message.userTaskList)
                                startActivity(intents)
                            }
                        }

                    } else {
                        activity.showMessage(activity, imgProfileBack, response.errorMessage.toString())
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(activity, imgProfileBack, response.errorMessage.toString())
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(activity, imgProfileBack, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(activity, imgProfileBack, response.errorMessage.toString())

            }
        })
    }

    override fun onBackPressed() {
        finish()
    }

}
