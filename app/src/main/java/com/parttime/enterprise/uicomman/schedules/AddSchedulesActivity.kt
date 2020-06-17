package com.parttime.enterprise.uicomman.schedules

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.*
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
import com.parttime.enterprise.uicomman.enrollcalander.ActivityEnrollCalander
import com.parttime.enterprise.uicomman.schedules.allenrolllist.GetAllEnrollUserListResponse
import com.parttime.enterprise.uicomman.schedules.allenrolllist.Message
import com.parttime.enterprise.validations.CreateScheduleValidation
import com.parttime.enterprise.viewmodels.AddScheduleViewModel
import kotlinx.android.synthetic.main.activity_add_schedule.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddSchedulesActivity : BaseActivity(), View.OnClickListener,
    UserEnrollListAdapter.ItemClickListener {

    private var countinous = 0
    private var isContinous = false
    lateinit var adapter: UserEnrollListAdapter
    lateinit var layoutManager: LinearLayoutManager
    var list = ArrayList<Message>()
    lateinit var viewModel: AddScheduleViewModel
    var selectedList = ArrayList<Message>()

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
        if (Utilities.isNetworkAvailable(this@AddSchedulesActivity)) {
            viewModel = ViewModelProviders.of(this@AddSchedulesActivity)
                .get(AddScheduleViewModel::class.java)
            viewModel.getUserEnrollListApi(
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                Utilities.isNetworkAvailable(this@AddSchedulesActivity)
            )
            getEnrollUserList()

        } else {
            showMessage(
                this@AddSchedulesActivity,
                enrollUserList,
                getString(R.string.network_error)
            )
        }

        // continuous check functionality
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
        adapter = UserEnrollListAdapter(activity, list, this@AddSchedulesActivity)
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
        addJobBack.setOnClickListener(this@AddSchedulesActivity)
        enrollCheckBoxesSaveAll.setOnClickListener(this@AddSchedulesActivity)
        imgAddAnotherDateView.setOnClickListener(this@AddSchedulesActivity)
        submitScheduleBtn.setOnClickListener(this@AddSchedulesActivity)
        imgCardHide6.setOnClickListener(this@AddSchedulesActivity)
        imgCardHide5.setOnClickListener(this@AddSchedulesActivity)
        imgCardHide4.setOnClickListener(this@AddSchedulesActivity)
        imgCardHide3.setOnClickListener(this@AddSchedulesActivity)
        imgCardHide2.setOnClickListener(this@AddSchedulesActivity)
        imgCardHide1.setOnClickListener(this@AddSchedulesActivity)
        txtDateCompleted.setOnClickListener ( this@AddSchedulesActivity )
        txtDateCompleted1.setOnClickListener ( this@AddSchedulesActivity )
        txtDateCompleted2.setOnClickListener ( this@AddSchedulesActivity )
        txtDateCompleted3.setOnClickListener ( this@AddSchedulesActivity )
        txtDateCompleted4.setOnClickListener ( this@AddSchedulesActivity )
        txtDateCompleted5.setOnClickListener ( this@AddSchedulesActivity )
        txtDateCompleted6.setOnClickListener ( this@AddSchedulesActivity )

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
            submitScheduleBtn -> {
                var scheduleDateArray = JSONArray()

                if (cardDate0.isVisible) {
                    var validate = CreateScheduleValidation().isInputValidJobAddJobs(
                        txtDateCompleted.text.toString(),
                        spn_startHour.selectedItem.toString() + ":" + spn_startmin.selectedItem.toString(),
                        spn_EndHour.selectedItem.toString() + ":" + spn_Endmin.selectedItem.toString(),
                        " ", " ", " ", this@AddSchedulesActivity
                    )
                    if (validate.status == 0) {
                        showMessage(
                            this@AddSchedulesActivity,
                            submitScheduleBtn,
                            validate.errMessage
                        )
                        return
                    } else {
                        var blockJsonObject = JSONObject()
                        blockJsonObject.put("date", txtDateCompleted.text.toString())
                        blockJsonObject.put(
                            "startTime",
                            spn_startHour.selectedItem.toString() + ":" + spn_startmin.selectedItem.toString()
                        )
                        blockJsonObject.put(
                            "endTime",
                            spn_EndHour.selectedItem.toString() + ":" + spn_Endmin.selectedItem.toString()
                        )
                        scheduleDateArray.put(blockJsonObject)
                    }

                }
                if (cardDate1.isVisible) {
                    var validate = CreateScheduleValidation().isInputValidJobAddJobs(
                        txtDateCompleted1.text.toString(),
                        startHour1.selectedItem.toString() + ":" + startMin1.selectedItem.toString(),
                        endHour1.selectedItem.toString() + ":" + endMin1.selectedItem.toString(),
                        " ", " ", " ", this@AddSchedulesActivity
                    )
                    if (validate.status == 0) {
                        showMessage(
                            this@AddSchedulesActivity,
                            submitScheduleBtn,
                            validate.errMessage
                        )
                        return
                    } else {
                        var blockJsonObject = JSONObject()
                        blockJsonObject.put("date", txtDateCompleted1.text.toString())
                        blockJsonObject.put(
                            "startTime",
                            startHour1.selectedItem.toString() + ":" + startMin1.selectedItem.toString()
                        )
                        blockJsonObject.put(
                            "endTime",
                            endHour1.selectedItem.toString() + ":" + endMin1.selectedItem.toString()
                        )
                        scheduleDateArray.put(blockJsonObject)
                    }

                }

                if (cardDate2.isVisible) {
                    var validate = CreateScheduleValidation().isInputValidJobAddJobs(
                        txtDateCompleted2.text.toString(),
                        startHour2.selectedItem.toString() + ":" + startMin2.selectedItem.toString(),
                        endHour2.selectedItem.toString() + ":" + endMin2.selectedItem.toString(),
                        " ", " ", " ", this@AddSchedulesActivity
                    )
                    if (validate.status == 0) {
                        showMessage(
                            this@AddSchedulesActivity,
                            submitScheduleBtn,
                            validate.errMessage
                        )
                        return
                    } else {
                        var blockJsonObject = JSONObject()
                        blockJsonObject.put("date", txtDateCompleted2.text.toString())
                        blockJsonObject.put(
                            "startTime",
                            startHour2.selectedItem.toString() + ":" + startMin2.selectedItem.toString()
                        )
                        blockJsonObject.put(
                            "endTime",
                            endHour2.selectedItem.toString() + ":" + endMin2.selectedItem.toString()
                        )
                        scheduleDateArray.put(blockJsonObject)
                    }

                }

                if (cardDate3.isVisible) {
                    var validate = CreateScheduleValidation().isInputValidJobAddJobs(
                        txtDateCompleted3.text.toString(),
                        startHour3.selectedItem.toString() + ":" + startMin3.selectedItem.toString(),
                        endHour3.selectedItem.toString() + ":" + endMin3.selectedItem.toString(),
                        " ", " ", " ", this@AddSchedulesActivity
                    )
                    if (validate.status == 0) {
                        showMessage(
                            this@AddSchedulesActivity,
                            submitScheduleBtn,
                            validate.errMessage
                        )
                        return
                    } else {
                        var blockJsonObject = JSONObject()
                        blockJsonObject.put("date", txtDateCompleted3.text.toString())
                        blockJsonObject.put(
                            "startTime",
                            startHour3.selectedItem.toString() + ":" + startMin3.selectedItem.toString()
                        )
                        blockJsonObject.put(
                            "endTime",
                            endHour3.selectedItem.toString() + ":" + endMin3.selectedItem.toString()
                        )
                        scheduleDateArray.put(blockJsonObject)
                    }

                }

                if (cardDate4.isVisible) {
                    var validate = CreateScheduleValidation().isInputValidJobAddJobs(
                        txtDateCompleted4.text.toString(),
                        startHour4.selectedItem.toString() + ":" + startMin4.selectedItem.toString(),
                        endHour4.selectedItem.toString() + ":" + endMin4.selectedItem.toString(),
                        " ", " ", " ", this@AddSchedulesActivity
                    )
                    if (validate.status == 0) {
                        showMessage(
                            this@AddSchedulesActivity,
                            submitScheduleBtn,
                            validate.errMessage
                        )
                        return
                    } else {
                        var blockJsonObject = JSONObject()
                        blockJsonObject.put("date", txtDateCompleted4.text.toString())
                        blockJsonObject.put(
                            "startTime",
                            startHour4.selectedItem.toString() + ":" + startMin4.selectedItem.toString()
                        )
                        blockJsonObject.put(
                            "endTime",
                            endHour4.selectedItem.toString() + ":" + endMin4.selectedItem.toString()
                        )
                        scheduleDateArray.put(blockJsonObject)
                    }

                }

                if (cardDate5.isVisible) {
                    var validate = CreateScheduleValidation().isInputValidJobAddJobs(
                        txtDateCompleted5.text.toString(),
                        startHour5.selectedItem.toString() + ":" + startMin5.selectedItem.toString(),
                        endHour5.selectedItem.toString() + ":" + endMin5.selectedItem.toString(),
                        " ", " ", " ", this@AddSchedulesActivity
                    )
                    if (validate.status == 0) {
                        showMessage(
                            this@AddSchedulesActivity,
                            submitScheduleBtn,
                            validate.errMessage
                        )
                        return
                    } else {
                        var blockJsonObject = JSONObject()
                        blockJsonObject.put("date", txtDateCompleted5.text.toString())
                        blockJsonObject.put(
                            "startTime",
                            startHour5.selectedItem.toString() + ":" + startMin5.selectedItem.toString()
                        )
                        blockJsonObject.put(
                            "endTime",
                            endHour5.selectedItem.toString() + ":" + endMin5.selectedItem.toString()
                        )
                        scheduleDateArray.put(blockJsonObject)
                    }

                }

                if (cardDate6.isVisible) {
                    var validate = CreateScheduleValidation().isInputValidJobAddJobs(
                        txtDateCompleted6.text.toString(),
                        startHour6.selectedItem.toString() + ":" + startMin6.selectedItem.toString(),
                        endHour6.selectedItem.toString() + ":" + endMin6.selectedItem.toString(),
                        " ", " ", " ", this@AddSchedulesActivity
                    )
                    if (validate.status == 0) {
                        showMessage(
                            this@AddSchedulesActivity,
                            submitScheduleBtn,
                            validate.errMessage
                        )
                        return
                    } else {
                        var blockJsonObject = JSONObject()
                        blockJsonObject.put("date", txtDateCompleted6.text.toString())
                        blockJsonObject.put(
                            "startTime",
                            startHour6.selectedItem.toString() + ":" + startMin6.selectedItem.toString()
                        )
                        blockJsonObject.put(
                            "endTime",
                            endHour6.selectedItem.toString() + ":" + endMin6.selectedItem.toString()
                        )
                        scheduleDateArray.put(blockJsonObject)
                    }

                }
                // api data map
                var map = HashMap<String, String>()
                map.put("scheduleDateTime", scheduleDateArray.toString())
                if (continousCheckBox.isChecked) {
                    map.put("isContinous", "1")
                } else {
                    map.put("isContinous", "0")
                }

                if (enrollCheckBoxesSaveAll.isChecked) {
                    var value: String = " "
                    val sb = StringBuilder()
                    for (i in 0 until list.size) {
                        if (list[i].isClicked) {
                            sb.append(list[i].userId).append(",")
                        }
                    }
                    if (sb.length > 2) {
                        value = sb.toString()
                        value = value.substring(0, value.length - 1)
                        map.put("userId", value)
                    }
                } else {
                    if (!selectedList.isNullOrEmpty()) {
                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until selectedList.size) {
                            if (selectedList[i].isClicked) {
                                sb.append(selectedList[i].userId).append(",")
                            }
                        }
                        if (sb.length > 0) {
                            value = sb.toString()
                            value = value.substring(0, value.length - 1)
                            map.put("userId", value)
                        }
                    }
                }
                if(map.containsKey("userId")){
                    // api hit
                    if (Utilities.isNetworkAvailable(this@AddSchedulesActivity)) {
                      //  showProgressBarNew()
                        viewModel.createSchedule(
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                            map,
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                            true
                        )
                        getScheduleResponse()
                        e("DATA",map.toString())
                    } else {
                        showMessage(
                            this@AddSchedulesActivity,
                            enrollCheckBoxesSaveAll,
                            getString(R.string.network_error)
                        )
                    }
                }else{
                    showMessage(this@AddSchedulesActivity,enrollCheckBoxesSaveAll,getString(R.string.err_enroll_user_list))
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
                    this@AddSchedulesActivity, getSelectedDate(txtDateCompleted, cal),
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
                    this@AddSchedulesActivity, getSelectedDate(txtDateCompleted1, cal),
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
                    this@AddSchedulesActivity, getSelectedDate(txtDateCompleted2, cal),
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
                    this@AddSchedulesActivity, getSelectedDate(txtDateCompleted3, cal),
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
                    this@AddSchedulesActivity, getSelectedDate(txtDateCompleted4, cal),
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
                    this@AddSchedulesActivity, getSelectedDate(txtDateCompleted5, cal),
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
                    this@AddSchedulesActivity, getSelectedDate(txtDateCompleted6, cal),
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
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(this@AddSchedulesActivity, enrollUserList, getString(msg))
        })
        viewModel.userEnrollListViewModels.observe(
            this,
            Observer { response: GetAllEnrollUserListResponse ->
                activity.hideProgressBarNew()
                if (response.success) {
                    if (response.code == 200) {
                        list.addAll(response.message)
                        adapter.notifyDataSetChanged()
                    } else if (response.code == 204) {
                        activity.hideProgressBarNew()
                        activity.showMessage(
                            this@AddSchedulesActivity,
                            enrollUserList,
                            response.errorMessage.toString()
                        )

                    } else {
                        activity.hideProgressBarNew()
                        activity.showMessage(
                            this@AddSchedulesActivity,
                            enrollUserList,
                            response.errorMessage.toString()
                        )
                    }
                } else {
                    activity.hideProgressBarNew()
                    activity.showMessage(
                        this@AddSchedulesActivity,
                        enrollUserList,
                        response.errorMessage.toString()
                    )
                }
            })

    }


    // get create schedule response
    private fun getScheduleResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(this@AddSchedulesActivity, enrollUserList, getString(msg))
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
                            this@AddSchedulesActivity,
                            enrollUserList,
                            response.errorMessage.toString()
                        )

                    } else {
                        activity.hideProgressBarNew()
                        activity.showMessage(
                            this@AddSchedulesActivity,
                            enrollUserList,
                            response.errorMessage.toString()
                        )
                    }
                } else {
                    activity.hideProgressBarNew()
                    activity.showMessage(
                        this@AddSchedulesActivity,
                        enrollUserList,
                        response.errorMessage.toString()
                    )
                }
            })

    }


    override fun onBackPressed() {
        val intent=Intent(this@AddSchedulesActivity,ActivityEnrollCalander::class.java)
        setResult(1001,intent)
        finish()
    }
}