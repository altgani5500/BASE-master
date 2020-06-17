package com.parttime.enterprise.uicomman.enrollworkerprofile.createtask

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.SuperEvaluatorAdaptor
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.EnrollWorkerProfileActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.superevaluatormodel.Message
import com.parttime.enterprise.uicomman.enrollworkerprofile.superevaluatorlist.SuperEvaluatorListActivity
import com.parttime.enterprise.viewmodels.CreateTaskViewModel
import kotlinx.android.synthetic.main.create_task_activity.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CreateTaskActivity : BaseActivity(), SuperEvaluatorAdaptor.OnItemClickAge,
    View.OnClickListener {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: SuperEvaluatorAdaptor
    var superEvaluatorList = ArrayList<Message>()
    lateinit var viewModel: CreateTaskViewModel
    var taskTypeSelection = 0
    var superEvaluatorId = 0

    var superEvaluatorListMore = ArrayList<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_task_activity)
        inItAdapter()
        initViews()

        // spinner Selection Hide Show
        spnaskTypes?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    dateSelectlayout.visibility = View.GONE
                    taskTypeSelection = 1
                } else if (position == 1) {
                    dateSelectlayout.visibility = View.VISIBLE
                    taskTypeSelection = 2
                }

            }

        }

        txtTaskDescription.setOnTouchListener(object : View.OnTouchListener {
          override fun onTouch(v: View, event: MotionEvent): Boolean {
              if (v.id == R.id.txtTaskDescription) {
                  v.parent.requestDisallowInterceptTouchEvent(true)
                  when (event.getAction() and MotionEvent.ACTION_MASK) {
                      MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                  }
              }
              return false
          }
      })


    }

    private fun initViews() {
        txtTaskDate.setOnClickListener(this@CreateTaskActivity)
        btnSubmitTask.setOnClickListener(this@CreateTaskActivity)
        accountSettingbck_add_user.setOnClickListener(this@CreateTaskActivity)
        evaluator_see_all.setOnClickListener(this@CreateTaskActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2001){
            if(data!=null){
                superEvaluatorId=data.getIntExtra("EVEID",0)

            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            evaluator_see_all -> {
                if (!superEvaluatorListMore.isNullOrEmpty()) {
                    val jsonString = Gson().toJson(superEvaluatorListMore)
                    val intent =
                        Intent(this@CreateTaskActivity, SuperEvaluatorListActivity::class.java)
                    intent.putExtra("MORE", jsonString)
                    intent.putExtra("FLAG",1)
                    startActivityForResult(intent,2001)
                }
            }
            txtTaskDate -> {
                var cal = Calendar.getInstance()
                var da = DatePickerDialog(
                    this@CreateTaskActivity, getSelectedDate(txtTaskDate, cal),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                da.datePicker.minDate = cal.timeInMillis
                da.show()
            }
            accountSettingbck_add_user -> {
                onBackPressed()
            }
            btnSubmitTask -> {
                if (taskTypeSelection != 0) {
                    if (superEvaluatorId != 0) {
                        if (dateSelectlayout.isVisible) {
                            if (!txtTaskDate.text.isNullOrEmpty()) {
                                var startTime = spn_startHour.selectedItem.toString().toInt()
                                var startMin = spn_startMin.selectedItem.toString().toInt()
                                var endTime = spn_startHour2.selectedItem.toString().toInt()
                                var endMin = spn_startMin2.selectedItem.toString().toInt()
                                if (endTime > startTime) {
                                    if (!txtTaskDescription.text.isNullOrEmpty() && txtTaskDescription.text.trim().length > 3) {
                                        if (edtTaskName.text.trim().length > 2) {
                                            var mapParam = HashMap<String, String>()
                                            mapParam.put(
                                                "userId",
                                                intent.getStringExtra("WORKERID")
                                            )
                                            mapParam.put("taskType", taskTypeSelection.toString())
                                            mapParam.put("taskName", edtTaskName.text.toString())
                                            mapParam.put(
                                                "taskDescription",
                                                txtTaskDescription.text.toString()
                                            )
                                            mapParam.put("evaluatorId", superEvaluatorId.toString())
                                            mapParam.put(
                                                "startTime",
                                                spn_startHour.selectedItem.toString() + ":" + spn_startMin.selectedItem.toString()
                                            )
                                            mapParam.put(
                                                "endTime",
                                                spn_startHour2.selectedItem.toString() + ":" +  spn_startMin2.selectedItem.toString()
                                            )
                                            mapParam.put("taskDate", txtTaskDate.text.toString())
                                            if (Utilities.isNetworkAvailable(this@CreateTaskActivity)) {
                                                showProgressBarNew()
                                                viewModel.createTaskThroughApi(
                                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                                                    mapParam,
                                                    Utilities.isNetworkAvailable(this@CreateTaskActivity)
                                                )
                                                getCreateTaskResponse()
                                            } else {
                                                showMessage(
                                                    this@CreateTaskActivity,
                                                    txtTaskDescription,
                                                    getString(R.string.network_error)
                                                )
                                            }
                                        } else {
                                            showMessage(
                                                this@CreateTaskActivity,
                                                superEvaluatorRecycle,
                                                getString(R.string.err_task_name)
                                            )
                                        }
                                    } else {
                                        showMessage(
                                            this@CreateTaskActivity,
                                            superEvaluatorRecycle,
                                            getString(R.string.task_description_err)
                                        )
                                    }
                                } else {
                                    showMessage(
                                        this@CreateTaskActivity,
                                        superEvaluatorRecycle,
                                        getString(R.string.start_time_end_time_validation)
                                    )
                                }

                            } else {
                                showMessage(
                                    this@CreateTaskActivity, superEvaluatorRecycle, getString(
                                        R.string.please_select_task_date
                                    )
                                )
                            }
                        } else {
                            if (!txtTaskDescription.text.isNullOrEmpty() && txtTaskDescription.text.trim().length > 3) {
                                if (edtTaskName.text.trim().length > 2) {
                                    var mapParam = HashMap<String, String>()
                                    mapParam.put("userId", intent.getStringExtra("WORKERID"))
                                    mapParam.put("taskType", taskTypeSelection.toString())
                                    mapParam.put("taskName", edtTaskName.text.toString())
                                    mapParam.put(
                                        "taskDescription",
                                        txtTaskDescription.text.toString()
                                    )
                                    mapParam.put("evaluatorId", superEvaluatorId.toString())
                                    Log.e("SERVER", mapParam.toString())
                                    if (Utilities.isNetworkAvailable(this@CreateTaskActivity)) {
                                        showProgressBarNew()
                                        viewModel.createTaskThroughApi(
                                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                                            mapParam,
                                            Utilities.isNetworkAvailable(this@CreateTaskActivity)
                                        )
                                        getCreateTaskResponse()
                                    } else {
                                        showMessage(
                                            this@CreateTaskActivity,
                                            txtTaskDescription,
                                            getString(R.string.network_error)
                                        )
                                    }
                                } else {
                                    showMessage(
                                        this@CreateTaskActivity,
                                        superEvaluatorRecycle,
                                        getString(R.string.err_task_name)
                                    )
                                }
                            } else {
                                showMessage(
                                    this@CreateTaskActivity,
                                    superEvaluatorRecycle,
                                    getString(R.string.task_description_err)
                                )
                            }
                        }

                    } else {
                        showMessage(
                            this@CreateTaskActivity,
                            superEvaluatorRecycle,
                            getString(R.string.please_select_super_evaluator)
                        )
                    }
                }
            }
        }
    }


    private fun inItAdapter() {
        adapter = SuperEvaluatorAdaptor(activity, superEvaluatorList, this@CreateTaskActivity)
        superEvaluatorRecycle.adapter = adapter
        // layoutManager = LinearLayoutManager(activity)
        layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        superEvaluatorRecycle.layoutManager = layoutManager

        if (Utilities.isNetworkAvailable(this@CreateTaskActivity)) {
            viewModel = ViewModelProviders.of(this).get(CreateTaskViewModel::class.java)
            viewModel.getSuperEvaluatorList(
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                Utilities.isNetworkAvailable(this@CreateTaskActivity)
            )
            getEvaluatorListData()
        } else {
            showMessage(
                this@CreateTaskActivity,
                superEvaluatorRecycle,
                getString(R.string.network_error)
            )
        }
    }

    override fun onClickAge(value: Message) {
        superEvaluatorId = value.evaluatorId
        //toast(value.toString())
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


    private fun getEvaluatorListData() {
        hideProgressBarNew()
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer {
            hideProgressBarNew()
            showMessage(this@CreateTaskActivity, superEvaluatorRecycle, getString(it))
        })

        viewModel.createTaskViewModel.observe(
            this, androidx.lifecycle.Observer {
                hideProgressBarNew()
                if (it.success) {
                    if (it.code == 200) {
                        superEvaluatorListMore.addAll(it.message)
                        if (it.message.size > 7) {
                            for (i in 0 until it.message.size) {
                                if (i < 7) {
                                    superEvaluatorList.add(it.message[i])
                                    adapter.notifyDataSetChanged()
                                } else {
                                    break
                                }
                            }
                        } else {
                            superEvaluatorList.addAll(it.message)
                            adapter.notifyDataSetChanged()
                        }

                    } else {
                        showMessage(
                            this@CreateTaskActivity,
                            superEvaluatorRecycle,
                            it.errorMessage.toString()
                        )
                    }

                } else {
                    showMessage(
                        this@CreateTaskActivity,
                        superEvaluatorRecycle,
                        it.errorMessage.toString()
                    )
                }
            }
        )


    }

    private fun getCreateTaskResponse() {
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer {
            hideProgressBarNew()
            showMessage(this@CreateTaskActivity, superEvaluatorRecycle, getString(it))
        })

        viewModel.createTaskViewModel2.observe(
            this, androidx.lifecycle.Observer {
                hideProgressBarNew()
                if (it.success) {
                    if (it.code == 200) {
                        showToast(it.message.toString())
                        onBackPressed()
                    } else {
                        hideProgressBarNew()
                        showMessage(
                            this@CreateTaskActivity,
                            superEvaluatorRecycle,
                            it.errorMessage.toString()
                        )
                    }

                } else {
                    hideProgressBarNew()
                    showMessage(
                        this@CreateTaskActivity,
                        superEvaluatorRecycle,
                        it.errorMessage.toString()
                    )
                }
            }
        )


    }


    override fun onBackPressed() {
        var intent = Intent(this@CreateTaskActivity, EnrollWorkerProfileActivity::class.java)
        setResult(101, intent)
        finish()
    }

}