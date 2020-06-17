package com.parttime.enterprise.uicomman.fragments.jobsDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.MyFragmentPagerAdapterStringType
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseFragment
import com.parttime.enterprise.uicomman.editjobs.EditJobActivity
import com.parttime.enterprise.uicomman.fragments.jobsDetails.models.JobDetailsModelResponse
import com.parttime.enterprise.viewmodels.JobDetailsFragmentViewModel
import kotlinx.android.synthetic.main.activity_job_description.*

class JobDeatailsFragments : BaseFragment() {

    lateinit var viewModel: JobDetailsFragmentViewModel
    lateinit var adapter: MyFragmentPagerAdapterStringType
    lateinit var jobId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var s=arguments?.getString("JOBID")
        if (s != null) {
            jobId = s
        }// getArguments().getString("JOBID")

        return inflater.inflate(R.layout.activity_job_description, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ll_details_layout_header.visibility = View.GONE
        lledit_details.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        apiJobDetailsResponse()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity).get(JobDetailsFragmentViewModel::class.java)

        lledit_details.setOnClickListener {
            val intent = Intent(activity, EditJobActivity::class.java)
            intent.putExtra("JOBID", jobId)
            startActivity(intent)
        }

    }


    // job list get from api
    private fun apiJobDetailsResponse() {
        viewModel.detailsJobListApi(
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
            jobId,
            Utilities.isNetworkAvailable(activity),
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
        )
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, ll_details_layout_header, getString(msg))
        })
        viewModel.jobDetailsResponseModel.observe(this, Observer { response: JobDetailsModelResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        var myValue = response
                        prev_partTimeType.text = response.message.parttimeType
                        prev_jobTitle.text = response.message.jobTitle
                        prev_description.text = response.message.jobDescription
                        prev_location.text = response.message.jobLocation
                        prev_branch.text = response.message.branchName
                        prev_currencys.text = response.message.currency
                        // hourly rate condition
                        if (response.message.hoursRates.trim().contentEquals("None Of These")) {
                            hourly_rates.text = response.message.hoursePer
                        } else {
                            hourly_rates.text = response.message.hoursRates
                        }
                        if (response.message.workingHours.toString().contentEquals("Flexible")
                            || response.message.workingHours.toString().contentEquals("flexible")
                        ) {
                            prev_workingHours.textSize = resources.getDimension(R.dimen.sp_5)
                        }
                        prev_workingHours.text = response.message.workingHours
                        prev_totalHrPerWeek.text = response.message.totalHoursPerWeek
                        prev_noOfApplicants.text = response.message.noOfApplicants
                        prev_workExp.text = response.message.workExperience
                        prev_requirements.text = response.message.requirements
                        prev_workguide.text = response.message.workGuidence
                        prev_benefits.text = response.message.benifits

                        adapter = MyFragmentPagerAdapterStringType(activity, response.message.jobImages)
                        viewPager.adapter = adapter
                        tabIndicatorDots.setupWithViewPager(viewPager)
                        prev_map.setOnClickListener {
                            //val strUri = "http://maps.google.com/maps?q=loc:"+,(Label which you want)"
                            val strUri =
                                "http://maps.google.com/maps?q=loc:" + response.message.jobLat + "," + response.message.jobLong + " (" + "Part Time Enterprise" + ")"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
                            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
                            startActivity(intent)
                        }


                    } else {
                        activity.showMessage(context, ll_details_layout_header, response.errorMessage.toString())
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, ll_details_layout_header, response.errorMessage.toString())
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, ll_details_layout_header, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(context, ll_details_layout_header, response.errorMessage.toString())

            }
        })

    }


}