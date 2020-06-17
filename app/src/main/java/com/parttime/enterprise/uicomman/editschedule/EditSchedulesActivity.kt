package com.parttime.enterprise.uicomman.editschedule

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.UserEnrollListAdapter
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.ActivityCalanderTaskList
import com.parttime.enterprise.uicomman.schedules.CreateScheduleResponseModel
import com.parttime.enterprise.uicomman.schedules.allenrolllist.Message
import com.parttime.enterprise.validations.CreateScheduleValidation
import com.parttime.enterprise.viewmodels.AddScheduleViewModel
import kotlinx.android.synthetic.main.activity_add_schedule.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EditSchedulesActivity : BaseActivity(), View.OnClickListener,
    UserEnrollListAdapter.ItemClickListener {

    private var countinous = 0
    private var isContinous = false
    lateinit var adapter: UserEnrollListAdapter
    lateinit var layoutManager: LinearLayoutManager
    var list = ArrayList<Message>()
    lateinit var viewModel: AddScheduleViewModel
    var selectedList = ArrayList<Message>()

    // prev intent value
    private var scheduleTimingId: Int = 0
    private var name: String = " "
    private var prifilePicture: String = "NA"
    private var jobTitle: String = " "
    private var startTime: String = " "
    private var endTime: String = " "
    private var scheduleDate: String = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                ""
            ).equals("ar")
        ) {
            addJobBack.rotation = 180F
        } else {
            addJobBack.rotation = 0F
        }

        initViews()
        setAdapter()
        getEnrollUserList()
        // continuous check functionality
        continousCheckBox.visibility = View.GONE
        ll_Check.visibility = View.GONE
        continousCheckBox.isChecked = true
        continousCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //Do Whatever you want in isChecked
                cardDate0.visibility = View.VISIBLE
                /*  cardDate1.visibility = View.GONE
                  cardDate2.visibility = View.GONE
                  cardDate3.visibility = View.GONE
                  cardDate4.visibility = View.GONE
                  cardDate5.visibility = View.GONE
                  cardDate6.visibility = View.GONE*/

            }
        }
    }

    /* Method to set recyclerview adapter
           */
    private fun setAdapter() {
        adapter = UserEnrollListAdapter(activity, list, this@EditSchedulesActivity)
        enrollUserList.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        enrollUserList.layoutManager = layoutManager
    }

    override fun onItemClicked(messages: ArrayList<Message>) {
        //toast(messages.toString())
        selectedList.addAll(messages)
        for (i in 0 until messages.size) {
            if (!messages[i].isClicked) {
                enrollCheckBoxesSaveAll.isChecked = false
            }
        }
    }

    private fun initViews() {
       // pp_ll.setBackgroundColor(getColor(R.color.white))
        addJobBack.setOnClickListener(this@EditSchedulesActivity)
        enrollCheckBoxesSaveAll.setOnClickListener(this@EditSchedulesActivity)
        imgAddAnotherDateView.setOnClickListener(this@EditSchedulesActivity)
        submitScheduleBtn22.setOnClickListener(this@EditSchedulesActivity)
        imgCardHide6.setOnClickListener(this@EditSchedulesActivity)
        imgCardHide5.setOnClickListener(this@EditSchedulesActivity)
        imgCardHide4.setOnClickListener(this@EditSchedulesActivity)
        imgCardHide3.setOnClickListener(this@EditSchedulesActivity)
        imgCardHide2.setOnClickListener(this@EditSchedulesActivity)
        imgCardHide1.setOnClickListener(this@EditSchedulesActivity)
        txtDateCompleted.setOnClickListener(this@EditSchedulesActivity)
        txtDateCompleted1.setOnClickListener(this@EditSchedulesActivity)
        txtDateCompleted2.setOnClickListener(this@EditSchedulesActivity)
        txtDateCompleted3.setOnClickListener(this@EditSchedulesActivity)
        txtDateCompleted4.setOnClickListener(this@EditSchedulesActivity)
        txtDateCompleted5.setOnClickListener(this@EditSchedulesActivity)
        txtDateCompleted6.setOnClickListener(this@EditSchedulesActivity)

    }

    override fun onResume() {
        super.onResume()
        checkDateMultipleVisible()
    }

    private fun checkDateMultipleVisible() {
        if (cardDate1.isVisible || cardDate2.isVisible || cardDate3.isVisible || cardDate4.isVisible
            || cardDate5.isVisible || cardDate6.isVisible
        ) {
            continousCheckBox.isChecked = false
            isContinous = false
        } else {
            continousCheckBox.isChecked = true
            isContinous = true
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            submitScheduleBtn22 -> {
                var map = HashMap<String, String>()
                if (cardDate0.isVisible) {
                    var s = txtDateCompleted.text.toString()
                    var validate = CreateScheduleValidation().isInputValidJobAddJobs(
                        s,
                        spn_startHour.selectedItem.toString() + ":" + spn_startmin.selectedItem.toString(),
                        spn_EndHour.selectedItem.toString() + ":" + spn_Endmin.selectedItem.toString(),
                        " ", " ", " ", this@EditSchedulesActivity
                    )

                    if (validate.status == 0) {
                        showMessage(
                            this@EditSchedulesActivity,
                            submitScheduleBtn22,
                            validate.errMessage
                        )
                        return
                    } else {
                        map.put("date", s)
                        map.put(
                            "startTime",
                            spn_startHour.selectedItem.toString() + ":" + spn_startmin.selectedItem.toString()
                        )
                        map.put(
                            "endTime",
                            spn_EndHour.selectedItem.toString() + ":" + spn_Endmin.selectedItem.toString()
                        )

                    }

                }

                map.put("scheduleTimingId", scheduleTimingId.toString())
                if (map.containsKey("scheduleTimingId")) {
                    // api hit
                    var v = map.get("scheduleTimingId")
                    if (v!!.contains(",")) {
                        var sspl = v.split(",")
                        map.put("scheduleTimingId", sspl[0])
                    }
                    if (Utilities.isNetworkAvailable(this@EditSchedulesActivity)) {
                        viewModel = ViewModelProviders.of(this@EditSchedulesActivity)
                            .get(AddScheduleViewModel::class.java)
                        showProgressBarNew()
                        viewModel.editSchedule(
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                            map,
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                            true
                        )
                        getScheduleResponse()
                    } else {
                        showMessage(
                            this@EditSchedulesActivity,
                            enrollCheckBoxesSaveAll,
                            getString(R.string.network_error)
                        )
                    }
                } else {
                    showMessage(
                        this@EditSchedulesActivity,
                        enrollCheckBoxesSaveAll,
                        getString(R.string.err_enroll_user_list)
                    )
                }


            }
            enrollCheckBoxesSaveAll -> {
                if (!list.isNullOrEmpty()) {
                    for (i in 0 until list.size) {
                        if (!list[i].isClicked) {
                            list[i].isClicked = true
                        }
                    }
                    enrollCheckBoxesSaveAll.isChecked = true
                    adapter.notifyDataSetChanged()
                }
            }
            addJobBack -> {
                onBackPressed()
            }
            imgCardHide1 -> {
                if (cardDate1.isVisible) {
                    cardDate1.visibility = View.GONE
                }
                checkDateMultipleVisible()
            }
            imgCardHide2 -> {
                if (cardDate2.isVisible) {
                    cardDate2.visibility = View.GONE
                }
                checkDateMultipleVisible()
            }
            imgCardHide3 -> {
                if (cardDate3.isVisible) {
                    cardDate3.visibility = View.GONE
                }
                checkDateMultipleVisible()
            }
            imgCardHide4 -> {
                if (cardDate4.isVisible) {
                    cardDate4.visibility = View.GONE
                }
            }
            imgCardHide5 -> {
                if (cardDate5.isVisible) {
                    cardDate5.visibility = View.GONE
                }
                checkDateMultipleVisible()
            }
            imgCardHide6 -> {
                if (cardDate6.isVisible) {
                    cardDate6.visibility = View.GONE
                }
                checkDateMultipleVisible()
            }
            imgAddAnotherDateView -> {
                if (!cardDate1.isVisible) {
                    cardDate1.visibility = View.VISIBLE
                } else if (!cardDate2.isVisible) {
                    cardDate2.visibility = View.VISIBLE
                } else if (!cardDate3.isVisible) {
                    cardDate3.visibility = View.VISIBLE
                } else if (!cardDate4.isVisible) {
                    cardDate4.visibility = View.VISIBLE
                } else if (!cardDate5.isVisible) {
                    cardDate5.visibility = View.VISIBLE
                } else if (!cardDate6.isVisible) {
                    cardDate6.visibility = View.VISIBLE
                }
                checkDateMultipleVisible()
            }
            txtDateCompleted -> {
                var cal = Calendar.getInstance()
                var da = DatePickerDialog(
                    this@EditSchedulesActivity, getSelectedDate(txtDateCompleted, cal),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                da.datePicker.minDate = cal.timeInMillis
                da.show()
            }
            txtDateCompleted1 -> {
                var cal = Calendar.getInstance()
                var da = DatePickerDialog(
                    this@EditSchedulesActivity, getSelectedDate(txtDateCompleted1, cal),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                da.datePicker.minDate = cal.timeInMillis
                da.show()
            }
            txtDateCompleted2 -> {
                var cal = Calendar.getInstance()
                var da = DatePickerDialog(
                    this@EditSchedulesActivity, getSelectedDate(txtDateCompleted2, cal),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                da.datePicker.minDate = cal.timeInMillis
                da.show()
            }
            txtDateCompleted3 -> {
                var cal = Calendar.getInstance()
                var da = DatePickerDialog(
                    this@EditSchedulesActivity, getSelectedDate(txtDateCompleted3, cal),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                da.datePicker.minDate = cal.timeInMillis
                da.show()
            }
            txtDateCompleted4 -> {
                var cal = Calendar.getInstance()
                var da = DatePickerDialog(
                    this@EditSchedulesActivity, getSelectedDate(txtDateCompleted4, cal),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                da.datePicker.minDate = cal.timeInMillis
                da.show()
            }
            txtDateCompleted5 -> {
                var cal = Calendar.getInstance()
                var da = DatePickerDialog(
                    this@EditSchedulesActivity, getSelectedDate(txtDateCompleted5, cal),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                da.datePicker.minDate = cal.timeInMillis
                da.show()
            }
            txtDateCompleted6 -> {
                var cal = Calendar.getInstance()
                var da = DatePickerDialog(
                    this@EditSchedulesActivity, getSelectedDate(txtDateCompleted6, cal),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                da.datePicker.minDate = cal.timeInMillis
                da.show()
            }
        }
    }

    // get Date picker for dialog
    private fun getSelectedDate(
        textView: TextView,
        cal: Calendar
    ): DatePickerDialog.OnDateSetListener? {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                textView.text = sdf.format(cal.time)
            }
        return dateSetListener
    }


    // get All User Enroll List
    private fun getEnrollUserList() {
        continousCheckBox.visibility = View.VISIBLE
        submitScheduleBtn22.visibility=View.VISIBLE
        submitScheduleBtn.visibility=View.GONE
        ll_enrollView.visibility = View.GONE
        scheduleTimingId = intent.getStringExtra("SCHEDULE_TIMING").toInt()
        name = intent.getStringExtra("NAME")
        prifilePicture = intent.getStringExtra("PROFILE_IMG")
        jobTitle = intent.getStringExtra("JOB_TITLE")
        startTime = intent.getStringExtra("START_TIME")
        endTime = intent.getStringExtra("END_TIME")
        scheduleDate = intent.getStringExtra("SCHEDULE_DATE")
        jobAddTitle_txt.text = getString(R.string.edit_schedule)
        imgAddAnotherDateView.visibility = View.INVISIBLE
        txtDateCompleted.text = scheduleDate
        var splistStartDate = startTime.split(":")
        var splistEndDate = endTime.split(":")
        for (i in resources.getStringArray(R.array.time_hour).indices) {
            if (resources.getStringArray(R.array.time_hour)[i].contentEquals(splistStartDate[0])) {
                spn_startHour.setSelection(i)
                break
            }
        }
        for (j in resources.getStringArray(R.array.time_min).indices) {
            if (resources.getStringArray(R.array.time_min)[j].contentEquals(splistStartDate[1])) {
                spn_startmin.setSelection(j)
                break
            }
        }
        // for end time
        for (k in resources.getStringArray(R.array.time_hour).indices) {
            if (resources.getStringArray(R.array.time_hour)[k].contentEquals(splistEndDate[0])) {
                spn_EndHour.setSelection(k)
                break
            }
        }
        for (m in resources.getStringArray(R.array.time_min).indices) {
            if (resources.getStringArray(R.array.time_min)[m].contentEquals(splistEndDate[1])) {
                spn_Endmin.setSelection(m)
                break
            }
        }
        list.add(Message(jobTitle, name, prifilePicture, scheduleTimingId, false))
        // list.addAll(response.message)
        adapter.notifyDataSetChanged()

    }


    // get create schedule response
    private fun getScheduleResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(this@EditSchedulesActivity, enrollUserList, getString(msg))
        })
        viewModel.createScheduleViewModel.observe(
            this,
            Observer { response: CreateScheduleResponseModel ->
                activity.hideProgressBarNew()
                if (response.success) {
                    if (response.code == 200) {
                        showToast(response.message)
                        onBackPressed()
                    } else if (response.code == 204) {
                        activity.hideProgressBarNew()
                        activity.showMessage(
                            this@EditSchedulesActivity,
                            enrollUserList,
                            response.errorMessage.toString()
                        )

                    } else {
                        activity.hideProgressBarNew()
                        activity.showMessage(
                            this@EditSchedulesActivity,
                            enrollUserList,
                            response.errorMessage.toString()
                        )
                    }
                } else {
                    activity.hideProgressBarNew()
                    activity.showMessage(
                        this@EditSchedulesActivity,
                        enrollUserList,
                        response.errorMessage.toString()
                    )
                }
            })

    }


    override fun onBackPressed() {
        val intent = Intent(this@EditSchedulesActivity, ActivityCalanderTaskList::class.java)
        setResult(1003, intent)
        finish()
    }
}