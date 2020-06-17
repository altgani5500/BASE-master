package com.parttime.enterprise.uicomman.fragments.jobslist

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.JobsListAdapter
import com.parttime.enterprise.helpers.Utilities.isNetworkAvailable
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseFragment
import com.parttime.enterprise.uicomman.addjobs.AddJobsActivity
import com.parttime.enterprise.uicomman.appsearch.AppSearchActivity
import com.parttime.enterprise.uicomman.auth.devicetokenmodels.DeviceToken
import com.parttime.enterprise.uicomman.editjobs.EditJobActivity
import com.parttime.enterprise.uicomman.fragments.jobslist.jobsdelete.DeleteJobResponse
import com.parttime.enterprise.uicomman.fragments.jobslist.jobslistmodels.JobListResponseModel
import com.parttime.enterprise.uicomman.fragments.jobslist.jobslistmodels.Message
import com.parttime.enterprise.uicomman.jobdetails.JobDetailsActivity
import com.parttime.enterprise.uicomman.notifications.NotificationListActivity
import com.parttime.enterprise.viewmodels.JobListFragmentViewModel
import kotlinx.android.synthetic.main.frgmengt_jobs.*

class JobsFragment : BaseFragment(), JobsListAdapter.OnItemClickForJobFragment {

    lateinit var callback: OnNotifyRelodPage
    lateinit var viewModel: JobListFragmentViewModel
    lateinit var adapter: JobsListAdapter
    var list = ArrayList<Message>()
    lateinit var layoutManager: LinearLayoutManager


    fun setOnHeadlineSelectedListener(callback: OnNotifyRelodPage) {
        this.callback = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnNotifyRelodPage {
        fun reloadPage(position: Int)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.frgmengt_jobs, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity).get(JobListFragmentViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setAdapter()

    }

    private fun getDeviceTokenResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.showMessage(activity, top_ll, getString(msg))
        })

        // get Response through observer
        viewModel.deviceTokenResponse.observe(this, Observer { response: DeviceToken ->
            if (response.success) {
                Log.e("Device Token APi Res", response.toString())
                if (response.code == 200) {
                    if (response.success) {
                        //activity.showMessage(activity, top_ll, response.message.toString())
                    } else {
                        activity.showMessage(
                            activity,
                            top_ll,
                            response.error_message.toString()
                        )
                    }
                } else {

                    activity.showMessage(activity, top_ll, response.error_message.toString())
                }
            } else {

                activity.showMessage(activity, top_ll, response.error_message.toString())

            }
        })
    }

    // job list get from api
    private fun apiJobListResponse() {
        activity.hideProgressBarNew()
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, top_ll, getString(msg))
        })
        viewModel.jobListResponseModel.observe(this, Observer { response: JobListResponseModel ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    if (activity.appPrefence.getString(
                            AppPrefence.SharedPreferncesKeys.apiToken.toString(),
                            ""
                        ).length > 2
                        && activity.appPrefence.getString(
                            AppPrefence.SharedPreferncesKeys.email.toString(),
                            ""
                        ).length > 5
                    ) {
                        // default Token Set
                        Log.e(
                            "DEVKEY",
                            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
                        )

                        // here we need To Call Adapter
                        if (response.success) {
                            if (!list.isEmpty())
                                list.clear()
                            list.addAll(response.message)
                            adapter.notifyDataSetChanged()
                            checkJobList()
                        } else {
                            activity.showMessage(context, top_ll, response.errorMessage.toString())
                            checkJobList()
                        }
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

    /*
         Method to set recyclerview adapter
         */
    private fun setAdapter() {
        adapter = JobsListAdapter(activity, list, this@JobsFragment)
        jobsRecycleView.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        jobsRecycleView.layoutManager = layoutManager
    }


    override fun onResume() {
        super.onResume()

        activity.showProgressBarNew()
        viewModel.getJobListApi(
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
            ,
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
            isNetworkAvailable(activity)
        )
        apiJobListResponse()
        checkJobList()

        // device token update
        if (activity.appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                "1"
            ).contentEquals("1")
        ) {
            if (!activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.deviceToken.toString()).isNullOrBlank()) {
                viewModel.deviceTokenApi(
                    activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.deviceToken.toString()),
                    "android",
                    isNetworkAvailable(context),
                    activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                    activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                )
                // get Device Token Response
                getDeviceTokenResponse()
            }
        }
    }

    /*
        Method to set onClick listener
         */
    private fun setOnClickListener() {
        fab_btn.setOnClickListener {
            val intent = Intent(context, AddJobsActivity::class.java)
            startActivity(intent)
        }
        jobSearch.setOnClickListener {
            if(geAccountType()==1 || geAccountType()==2){
                activity.showMessage(activity,jobSearch,getString(R.string.err_account_type))
            }else if(geAccountType()==3){
                activity.launchActivity(AppSearchActivity::class.java)
            }
        }

        jobNotify.setOnClickListener {
            activity.launchActivity(NotificationListActivity::class.java)
        }


    }


    // deletet Job in the list and api
    private fun apiDelJobListResponse() {
        activity.hideProgressBarNew()
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, top_ll, getString(msg))
        })
        viewModel.delModelResponse.observe(this, Observer { response: DeleteJobResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        activity.showMessage(context, top_ll, response.message.toString())
                        callback.reloadPage(1)
                    } else {
                        activity.showMessage(context, top_ll, response.errorMessage.toString())
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, top_ll, response.errorMessage.toString())
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, top_ll, response.errorMessage.toString())
                } else if (response.code == 422) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, top_ll, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(context, top_ll, response.errorMessage.toString())

            }
        })

    }

    /*Custom Dialog For Delete Before*/
    fun showDialogBeforDelete(msg: String, pro: Message) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_for_sure_twobtn)
        val body = dialog.findViewById(R.id.dialog_txtTitle) as TextView
        body.text = msg
        val yesBtn = dialog.findViewById(R.id.dialog_yes) as Button
        val noBtn = dialog.findViewById(R.id.dialog_cancel) as Button
        yesBtn.setOnClickListener {
            activity.showProgressBarNew()
            viewModel.delJobListApi(
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
                ,
                pro.jobId.toString(),
                isNetworkAvailable(activity),
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
            )

            apiDelJobListResponse()
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickDelete(value: Message) {
        showDialogBeforDelete(activity.resources.getString(R.string.are_you_sure_you_want_to_delete), value)
    }

    override fun onClickEdit(value: Message) {
        val intent = Intent(activity, EditJobActivity::class.java)
        intent.putExtra("JOBID", value.jobId.toString())
        startActivity(intent)
    }

    override fun onClickDetails(value: Message) {
        val intent = Intent(activity, JobDetailsActivity::class.java)
        intent.putExtra("JOBID", value.jobId.toString())
        startActivity(intent)
    }

    private fun checkJobList() {
        if (list.isNullOrEmpty()) {
            ll_empty_list_home.visibility = View.VISIBLE
            jobsRecycleView.visibility = View.GONE
        } else {
            jobsRecycleView.visibility = View.VISIBLE
            ll_empty_list_home.visibility = View.GONE
        }
    }


}