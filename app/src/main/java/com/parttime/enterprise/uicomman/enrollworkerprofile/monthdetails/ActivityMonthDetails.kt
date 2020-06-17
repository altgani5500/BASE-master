package com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.TaskHistoryDayAdapter2
import com.parttime.enterprise.adapters.WeekHistoryListAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.AcceptRejectWorkResponse
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.Message
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.MonthDetailsModelResponse
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.Work
import com.parttime.enterprise.viewmodels.MonthDetailsViewModel
import kotlinx.android.synthetic.main.activity_month_details.*

class ActivityMonthDetails : BaseActivity(), TaskHistoryDayAdapter2.OnItemClickDayHistory2,
    View.OnClickListener, WeekHistoryListAdapter.OnItemClickWork {


    lateinit var layoutManagerBtn: LinearLayoutManager
    lateinit var adapterBtn: TaskHistoryDayAdapter2
    var btnList = ArrayList<Message>()
    lateinit var viewModel: MonthDetailsViewModel
    private var intentUserID: String = ""

    // work history details
    lateinit var layoutManagerWokList: LinearLayoutManager
    lateinit var adapterWorkList: WeekHistoryListAdapter
    var btnWorkList = ArrayList<Work>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_details)
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                ""
            ).equals("ar")
        ) {
            moreBackMonth.rotation = 180F
        } else {
            moreBackMonth.rotation = 0F
        }
        moreBackMonth.setOnClickListener(this@ActivityMonthDetails)
        // set title
        toolBarTitleMore.text = intent.getStringExtra("MM")
        intentUserID = intent.getStringExtra("USERID")
        initHistoryAdapter()
    }

    override fun onClick(v: View?) {
        when (v) {
            moreBackMonth -> {
                onBackPressed()
            }
        }
    }

    override fun onClickDayHistory(values: ArrayList<Message>) {
        //showToast(values.toString())
        if (!btnWorkList.isNullOrEmpty()) {
            btnWorkList.clear()
        }
        for (i in 0 until values.size) {
            if (values[i].isClicked) {
                txtWeeksHours.text =
                    values[i].workHourDone.toString() + " " + getString(R.string.hrsss) + " /" + values[i].workHourSchedule.toString() + " " + getString(
                        R.string.hrsss
                    )
                for (j in 0 until values[i].work.size) {
                    btnWorkList.add(values[i].work[j])
                }
                break
            }
        }
        adapterWorkList.notifyDataSetChanged()
    }

    private fun initHistoryAdapter() {
        adapterBtn = TaskHistoryDayAdapter2(activity, btnList, this@ActivityMonthDetails)
        weekRecycleView.adapter = adapterBtn
        // layoutManager = LinearLayoutManager(activity)
        layoutManagerBtn = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        weekRecycleView.layoutManager = layoutManagerBtn

        // work list
        adapterWorkList = WeekHistoryListAdapter(activity, btnWorkList, this@ActivityMonthDetails)
        weekListRecycleView.adapter = adapterWorkList
        // layoutManager = LinearLayoutManager(activity)
        layoutManagerWokList = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        weekListRecycleView.layoutManager = layoutManagerWokList

    }

    override fun onClickWork(value: String, userPunchId: Int) {
        if (Utilities.isNetworkAvailable(this@ActivityMonthDetails)) {
            showProgressBarNew()
            var map = HashMap<String, String>()
            map.put("userPunchId", userPunchId.toString())
            map.put("status", value)
            viewModel.hitForAccepOrReject(
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                map,
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                true
            )
            getWorkHistoryStatus()

        } else {
            showMessage(
                this@ActivityMonthDetails,
                weekRecycleView,
                getString(R.string.network_error)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        hitWorkHistory()
    }

    private fun hitWorkHistory() {
        if (Utilities.isNetworkAvailable(this@ActivityMonthDetails)) {
            showProgressBarNew()
            viewModel = ViewModelProviders.of(this@ActivityMonthDetails)
                .get(MonthDetailsViewModel::class.java)
            var s = intent.getStringExtra("MM").split("-")
            viewModel.getWorkReportHistory(
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                intentUserID, getMonthMMFromString(s[0].trim()), s[1].toString().trim(), true
            )
            getWorkHistory()
        } else {
            showMessage(
                this@ActivityMonthDetails,
                weekRecycleView,
                getString(R.string.network_error)
            )
        }
    }

    override fun onBackPressed() {
        finish()
    }


    private fun getMonthMMFromString(month: String): String {
        var months = resources.getStringArray(R.array.years)
        if (month.equals(months[0])) {
            return "01"
        } else if (month.equals(months[1])) {
            return "02"
        } else if (month.equals(months[2])) {
            return "03"
        } else if (month.equals(months[3])) {
            return "04"
        } else if (month.equals(months[4])) {
            return "05"
        } else if (month.equals(months[5])) {
            return "06"
        } else if (month.equals(months[6])) {
            return "07"
        } else if (month.equals(months[7])) {
            return "08"
        } else if (month.equals(months[8])) {
            return "09"
        } else if (month.equals(months[9])) {
            return "10"
        } else if (month.equals(months[10])) {
            return "11"
        } else if (month.equals(months[11])) {
            return "12"
        } else {
            return " "
        }
    }

    // get workHistory
    private fun getWorkHistory() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@ActivityMonthDetails, weekRecycleView, getString(msg))
        })
        viewModel.userWorkReportHistory.observe(
            this,
            Observer { response: MonthDetailsModelResponse ->
                hideProgressBarNew()
                if (response.success) {
                    if (response.code == 200) {
                        if (!btnList.isNullOrEmpty()) {
                            btnList.clear()
                        }
                        txtMoreMonthDesc.text =
                            response.totalDone.toString() + " " + getString(R.string.hrs_out_of) + " " + response.totalSchedule.toString() + " " + getString(
                                R.string.hrsaa
                            )
                        for (i in 0 until response.message.size) {
                            var m = response.message[i]
                            var ia = i + 1
                            m.named = "Week " + ia.toString()
                            if (i == 0) {
                                m.isClicked = true
                            }
                            btnList.add(m)
                        }
                        adapterBtn.notifyDataSetChanged()

                    } else if (response.code == 204) {
                        hideProgressBarNew()
                        showMessage(
                            this@ActivityMonthDetails,
                            weekRecycleView,
                            response.error_message.toString()

                        )

                    } else {
                        hideProgressBarNew()
                        showMessage(
                            this@ActivityMonthDetails,
                            weekRecycleView,
                            response.error_message.toString()
                        )
                    }
                } else {
                    hideProgressBarNew()
                    showMessage(
                        this@ActivityMonthDetails,
                        weekRecycleView,
                        response.error_message.toString()
                    )
                }
            })

    }


    // get response of accept reject status
    private fun getWorkHistoryStatus() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@ActivityMonthDetails, weekRecycleView, getString(msg))
        })
        viewModel.acceptRejectViewModel.observe(
            this,
            Observer { response: AcceptRejectWorkResponse ->
                hideProgressBarNew()
                if (response.success) {
                    if (response.code == 200) {
                        showToast(response.message)
                        hitWorkHistory()
                    } else if (response.code == 204) {
                        hideProgressBarNew()
                        showMessage(
                            this@ActivityMonthDetails,
                            weekRecycleView,
                            response.error_message.toString()

                        )

                    } else {
                        hideProgressBarNew()
                        showMessage(
                            this@ActivityMonthDetails,
                            weekRecycleView,
                            response.error_message.toString()
                        )
                    }
                } else {
                    hideProgressBarNew()
                    showMessage(
                        this@ActivityMonthDetails,
                        weekRecycleView,
                        response.error_message.toString()
                    )
                }
            })

    }


}