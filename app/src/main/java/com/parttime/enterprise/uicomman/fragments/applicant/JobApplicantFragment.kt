package com.parttime.enterprise.uicomman.fragments.applicant

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.ApplicantListAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.helpers.Utilities.isNetworkAvailable
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseFragment
import com.parttime.enterprise.uicomman.applicantprofile.ApplicantProfileActivity
import com.parttime.enterprise.uicomman.chat.singlechat.SingleChatActivity
import com.parttime.enterprise.uicomman.fragments.applicant.acceptfinal.AcceptInterviewResponse
import com.parttime.enterprise.uicomman.fragments.applicant.applicantmodel.ApplicantModelResponse
import com.parttime.enterprise.uicomman.fragments.applicant.applicantmodel.Message
import com.parttime.enterprise.uicomman.fragments.applicant.enrollaccept.EnrollAcceptModel
import com.parttime.enterprise.uicomman.fragments.jobslist.jobsdelete.DeleteJobResponse
import com.parttime.enterprise.viewmodels.JobApplicantViewModel
import kotlinx.android.synthetic.main.fragmengt_applicant.*


class JobApplicantFragment : BaseFragment(), ApplicantListAdapter.ApplicantOnItemClickForJobFragment {

    lateinit var callback: OnNotifyRelodPage
    lateinit var viewModel: JobApplicantViewModel
    lateinit var jobId: String
    lateinit var adapter: ApplicantListAdapter
    lateinit var layoutManager: LinearLayoutManager
    var list = ArrayList<Message>()
    var hashMap = HashMap<String, String>()

    fun setOnHeadlineSelectedListener(callback: OnNotifyRelodPage) {
        this.callback = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnNotifyRelodPage {
        fun reloadPage(position: Int)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var s=arguments?.getString("JOBID")
        if (s != null) {
            jobId = s
        }// getArguments().getString("JOBID")

        return inflater.inflate(R.layout.fragmengt_applicant, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this@JobApplicantFragment).get(JobApplicantViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    /* Method to set recyclerview adapter
            */
    private fun setAdapter() {
        adapter = ApplicantListAdapter(activity, list, this@JobApplicantFragment)
        applicant_recycleViw.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        applicant_recycleViw.layoutManager = layoutManager
    }


    override fun onResume() {
        super.onResume()
        apiApplicantListResponse()
    }

    // Applicant get from api
    private fun apiApplicantListResponse() {
        activity.showProgressBarNew()
        val storedHashMapString =
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.FILTER.toString(), " ")
        if (storedHashMapString.trim().isNotEmpty()) {
            if (!activity.getHashMapFromSharePrepence(storedHashMapString, Gson()).isNullOrEmpty()) {
                hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
            }
        }
        viewModel.applicantListApi(
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
            ,
            jobId,
            Utilities.isNetworkAvailable(activity),
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
            ,
            hashMap
        )


        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, applicant_recycleViw, getString(msg))
        })
        viewModel.applicangtResponseModel.observe(this, Observer { response: ApplicantModelResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        if (!list.isEmpty())
                            list.clear()
                        list.addAll(response.message)
                        adapter.notifyDataSetChanged()
                        if (list.isEmpty()) {
                            ll_empty_list.visibility = View.VISIBLE
                            applicant_recycleViw.visibility = View.GONE
                        } else {
                            applicant_recycleViw.visibility = View.VISIBLE
                            ll_empty_list.visibility = View.GONE
                        }
                    } else {
                        activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())

            }
        })

    }


    /*Custom Dialog For Final Acceptance*/
    fun showDialogBeforAcceptance(pro: Message) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.final_acceptance_dialog)
        val txtCountMax=dialog.findViewById(R.id.applicant_Accept_massage) as TextView
        val body = dialog.findViewById(R.id.final_acceptence_input) as EditText
        val yesBtn = dialog.findViewById(R.id.final_acceptence_btn) as Button
        val congrts = dialog.findViewById(R.id.applicant_Accepts) as TextView
        var count=txtCountMax.text.toString().trim()
        body.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
               if(s.toString().trim().isEmpty()){
                   txtCountMax.text=getString(R.string.thousand)
               }else if(s.toString().isNotEmpty()){
                   var tempCount=count.toInt()-s.length
                   txtCountMax.text=tempCount.toString()
               }

            }
        })
        congrts.setOnClickListener {
            if (body.text.toString().contains(resources.getString(R.string.congrts))) {
                activity.showMessage(
                    context,
                    applicant_recycleViw,
                    resources.getString(R.string.all_ready_added_congrts)
                )
            } else {
                body.setText(" " + resources.getString(R.string.congrts))
            }
        }

        yesBtn.setOnClickListener {
            if (isNetworkAvailable(activity)) {
                if (!body.text.toString().trim().isNullOrEmpty()) {
                    activity.showProgressBarNew()
                    viewModel.enrollAcceptApiSet(
                        activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                        pro.jobApplyId.toString(),
                        activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                        body.text.toString().trim(),
                        isNetworkAvailable(activity)
                    )
                    getEnrollResponse(pro.applicantId, pro.profilePicture, pro.name)
                    dialog.dismiss()
                } else {
                    activity.showMessage(context, applicant_recycleViw, getString(R.string.message_required))
                }
            } else {
                activity.showMessage(context, applicant_recycleViw, getString(R.string.network_error))
            }

        }

        dialog.show()
    }

    /*get Enroll Worker Accept Response*/

    private fun getEnrollResponse(id: Int, imgString: String, userName: String) {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, applicant_recycleViw, getString(msg))
        })
        viewModel.enrollAcceptModell.observe(this, Observer { response: EnrollAcceptModel ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        // activity.showMessage(context, applicant_recycleViw, response.message.toString())
                        val intent = Intent(activity, SingleChatActivity::class.java)
                        intent.putExtra("USERID", id)
                        intent.putExtra("IMG", imgString)
                        intent.putExtra("NAME", userName)
                        activity.startActivity(intent)
                        //activity.launchActivity(SingleChatActivity::class.java)
                        activity.finish()
                    } else {
                        activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())

            }
        })

    }


    /*get Accept Final Interview Response*/
    private fun getAcceptInterview(id: Int, imgString: String, userName: String) {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, applicant_recycleViw, getString(msg))
        })
        viewModel.acceptFinalInterview.observe(this, Observer { response: AcceptInterviewResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        activity.showMessage(context, applicant_recycleViw, response.message.toString())
                        // then redirect to chat activity is pending for future
                        val intent = Intent(activity, SingleChatActivity::class.java)
                        intent.putExtra("USERID", id)
                        intent.putExtra("IMG", imgString)
                        intent.putExtra("NAME", userName)
                        activity.startActivity(intent)
                        //activity.launchActivity(SingleChatActivity::class.java)
                        activity.finish()
                    } else {
                        activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())

            }
        })

    }


    /*Custom Dialog For interview Acceptance*/
    fun showDialogBeforAcceptanceInterview(pro: Message) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.accept_interview_dialog)
        val body = dialog.findViewById(R.id.interview_acceptence_input) as EditText
        val yesBtn = dialog.findViewById(R.id.interview_acceptence_btn) as Button


        yesBtn.setOnClickListener {
            if (isNetworkAvailable(context)) {
                if (!body.text.toString().trim().isNullOrEmpty()) {
                    activity.showProgressBarNew()
                    viewModel.acceptInterView(
                        activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                        pro.jobApplyId.toString(),
                        activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                        body.text.toString().trim(),
                        isNetworkAvailable(activity)
                    )
                    getAcceptInterview(pro.applicantId, pro.profilePicture, pro.name)
                    dialog.dismiss()
                } else {
                    activity.showMessage(context, applicant_recycleViw, getString(R.string.message_required))
                }
            } else {
                activity.showMessage(context, applicant_recycleViw, getString(R.string.network_error))
            }

        }

        dialog.show()
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
            Log.e("APPLICATION PROFILE ID", pro.jobApplyId.toString())
            viewModel.delApplibListApi(
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
                , pro.jobApplyId.toString(), isNetworkAvailable(activity)
            )

            apiDelApplicantListResponse()
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    // deletet Applicant in the list and api
    private fun apiDelApplicantListResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, applicant_recycleViw, getString(msg))
        })
        viewModel.applicantDelResponseModel.observe(this, Observer { response: DeleteJobResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        activity.showMessage(context, applicant_recycleViw, response.message.toString())
                        callback.reloadPage(2)
                        //activity.launchActivityMain(HomeActivity::class.java)
                    } else {
                        activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                    }

                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                } else if (response.code == 422) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(context, applicant_recycleViw, response.errorMessage.toString())

            }
        })

    }

    override fun onClickDelete(value: Message) {
        showDialogBeforDelete(activity.resources.getString(R.string.applicant_del_msg), value)
    }

    override fun onClickFinal(value: Message) {
        showDialogBeforAcceptance(value)
    }

    override fun onClickChat(value: Message) {
        val intent = Intent(activity, SingleChatActivity::class.java)
        intent.putExtra("USERID", value.applicantId)
        intent.putExtra("IMG", value.profilePicture)
        intent.putExtra("NAME", value.name)
        activity.startActivity(intent)
        activity.finish()
    }

    override fun onClickApplied(value: Message) {
        showDialogBeforAcceptanceInterview(value)
    }

    override fun onDetailsClick(value: Message) {
        if(geAccountType()==1 || geAccountType()==2){
            activity.showMessage(activity,applicant_recycleViw,getString(R.string.err_account_type))
        }else if(geAccountType()==3) {
            val intent = Intent(activity, ApplicantProfileActivity::class.java)
            intent.putExtra("ID", value.applicantId.toString())
            activity.startActivity(intent)
        }
    }


}