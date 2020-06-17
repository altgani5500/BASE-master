package com.parttime.enterprise.uicomman.enrollworkerprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
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
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.superevaluatormodel.Message
import com.parttime.enterprise.uicomman.enrollworkerprofile.superevaluatorlist.SuperEvaluatorListActivity
import com.parttime.enterprise.viewmodels.EditErollWorkertaskDetailViewModel
import kotlinx.android.synthetic.main.create_task_activity.*

class EditTaskActivity : BaseActivity(), View.OnClickListener,
    SuperEvaluatorAdaptor.OnItemClickAge {


    private var taskType = 0
    private var taskName = " "
    private var taskdesc = " "
    private var dates = " "
    private var startTime = " "
    private var endTime = " "
    private var superEvaluatorId = 0
    private lateinit var taskIds:String
    // view model
    lateinit var viewModel: EditErollWorkertaskDetailViewModel
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: SuperEvaluatorAdaptor
    var superEvaluatorList = ArrayList<Message>()
   // var editTaskTypeSelection = 0
   var superEvaluatorListMore = ArrayList<Message>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_task_activity)
        jobAddTitle_txt.visibility = View.GONE
        filterTitle.text = getString(R.string.edit_task)
        taskType = intent.getIntExtra("TTYPE", 0)
        taskName = intent.getStringExtra("TNAME")
        taskdesc = intent.getStringExtra("TDESC")
        dates = intent.getStringExtra("TDATE")
        taskIds=intent.getStringExtra("TID")
        if (!intent.getStringExtra("TSTIME").isNullOrEmpty()) {
            startTime = intent.getStringExtra("TSTIME")
            endTime = intent.getStringExtra("TETIME")
        }
        superEvaluatorId = intent.getIntExtra("TSE", 0)
        inItView()
        inItAdapter()


    }

    private fun inItView() {
        accountSettingbck_add_user.setOnClickListener(this@EditTaskActivity)
        btnSubmitTask.setOnClickListener(this@EditTaskActivity)
        evaluator_see_all.setOnClickListener(this@EditTaskActivity)

        // set prev data for update
        if (taskdesc.trim().length > 1) {
            txtTaskDescription.setText(taskdesc.toString())
        }
        if (taskType == 1) {
            spnaskTypes.setSelection(0)
        } else if (taskType == 2) {
            spnaskTypes.setSelection(1)
        }
        edtTaskName.setText(taskName)
        if (dates.trim().length > 1) {
            txtTaskDate.text = dates
            if (!startTime.isNullOrEmpty() && startTime.trim().length > 0) {
                var sttime = startTime.split(":")
                var ettime = endTime.split(":")
                var arrayStartTime = resources.getStringArray(R.array.time_hour)
                var arrayStartMin = resources.getStringArray(R.array.time_min)

                var arrayEndTime = resources.getStringArray(R.array.time_hour)
                var arrayEndMin = resources.getStringArray(R.array.time_min)
                // start time hour
                for (i in 0 until arrayStartTime.size) {
                    var ss:String="00"
                    if(sttime[0].toInt()>0 && sttime[0].toInt()<10){
                        ss="0"+sttime[0]
                    }else{
                        ss =sttime[0]
                    }
                    if (ss.contentEquals(arrayStartTime.get(i))) {
                        spn_startHour.setSelection(i)
                       // spn_startHour2.setSelection(i+7)
                        break
                    }
                }
                // start time min
                for (i in 0 until arrayStartMin.size) {
                    if (sttime[1].contentEquals(arrayStartMin.get(i))) {
                        spn_startMin.setSelection(i)
                        break
                    }
                }

                // end time hour

                for (i in 0 until arrayEndTime.size) {
                    var ss2:String="00"
                    if(ettime[0].toInt()>0 && ettime[0].toInt()<10){
                        ss2="0"+ettime[0]
                    }else{
                        ss2 =ettime[0]
                    }
                    if (ss2.contentEquals(arrayEndTime.get(i))) {
                        spn_startHour2.setSelection(i)
                        break
                    }
                }
                // end time min
                for (i in 0 until arrayEndMin.size) {
                    if (ettime[1].contentEquals(arrayEndMin.get(i))) {
                        spn_startMin2.setSelection(i)
                        break
                    }
                }
            }
        }


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
                    taskType = 1
                } else if (position == 1) {
                    dateSelectlayout.visibility = View.VISIBLE
                    taskType = 2
                }

            }

        }

    }

    override fun onClickAge(value: Message) {
        superEvaluatorId = value.evaluatorId
        // toast(value.toString())
    }

    private fun inItAdapter() {
        adapter = SuperEvaluatorAdaptor(activity, superEvaluatorList, this@EditTaskActivity)
        superEvaluatorRecycle.adapter = adapter
        // layoutManager = LinearLayoutManager(activity)
        layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        superEvaluatorRecycle.layoutManager = layoutManager

        if (Utilities.isNetworkAvailable(this@EditTaskActivity)) {
            viewModel =
                ViewModelProviders.of(this).get(EditErollWorkertaskDetailViewModel::class.java)
            viewModel.getSuperEvaluatorList(
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                Utilities.isNetworkAvailable(this@EditTaskActivity)
            )
            getEvaluatorListData()
        } else {
            showMessage(
                this@EditTaskActivity,
                superEvaluatorRecycle,
                getString(R.string.network_error)
            )
        }
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
                    if(!superEvaluatorList.isNullOrEmpty()){
                        for(i in 0 until superEvaluatorList.size){
                            if(superEvaluatorList[i].isClecked){
                                superEvaluatorList[i].isClecked=false
                                break
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }

                    val jsonString = Gson().toJson(superEvaluatorListMore)
                    val intent =
                        Intent(this@EditTaskActivity, SuperEvaluatorListActivity::class.java)
                    intent.putExtra("MORE", jsonString)
                    intent.putExtra("FLAG",2)
                    startActivityForResult(intent,2001)
                }
            }
            accountSettingbck_add_user -> {
                onBackPressed()
            }
            btnSubmitTask->{
                if(taskType!=0){
                    if(superEvaluatorId!=0){
                        if(dateSelectlayout.isVisible){
                            if(!txtTaskDate.text.isNullOrEmpty()){
                                var startTime=spn_startHour.selectedItem.toString().toInt()
                                var startMin=spn_startMin.selectedItem.toString().toInt()
                                var endTime=spn_startHour2.selectedItem.toString().toInt()
                                var endMin=spn_startMin2.selectedItem.toString().toInt()
                                if(endTime>startTime){
                                    if(!txtTaskDescription.text.isNullOrEmpty() && txtTaskDescription.text.trim().length>3){
                                        if(edtTaskName.text.trim().length>4){
                                            var mapParam=HashMap<String,String>()
                                            mapParam.put("taskId",taskIds.toString())
                                            mapParam.put("taskType",taskType.toString())
                                            mapParam.put("taskName",edtTaskName.text.toString())
                                            mapParam.put("taskDescription",txtTaskDescription.text.toString())
                                            mapParam.put("evaluatorId",superEvaluatorId.toString())
                                            mapParam.put("startTime",startTime.toString()+":"+startMin.toString())
                                            mapParam.put("endTime",endTime.toString()+":"+endMin.toString())
                                            mapParam.put("taskDate",txtTaskDate.text.toString())
                                            if(Utilities.isNetworkAvailable(this@EditTaskActivity)){
                                                showProgressBarNew()
                                                viewModel.updateTaskThroughApi(appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),mapParam,
                                                    Utilities.isNetworkAvailable(this@EditTaskActivity))
                                                getCreateTaskResponse()
                                            }else{
                                                showMessage(this@EditTaskActivity,txtTaskDescription,getString(R.string.network_error))
                                            }
                                        }else{
                                            showMessage(
                                                this@EditTaskActivity,
                                                superEvaluatorRecycle,
                                                getString(R.string.err_task_name)
                                            )
                                        }
                                    }else{
                                        showMessage(
                                            this@EditTaskActivity,
                                            superEvaluatorRecycle,
                                            getString(R.string.task_description_err)
                                        )
                                    }
                                }else{
                                    showMessage(
                                        this@EditTaskActivity,
                                        superEvaluatorRecycle,
                                        getString(R.string.start_time_end_time_validation)
                                    )
                                }

                            }else{
                                showMessage(this@EditTaskActivity,superEvaluatorRecycle,getString(
                                    R.string.please_select_task_date))
                            }
                        }else{
                            if(!txtTaskDescription.text.isNullOrEmpty() && txtTaskDescription.text.trim().length>3){
                                if(edtTaskName.text.trim().length>4){
                                    var mapParam=HashMap<String,String>()
                                    mapParam.put("taskId",taskIds.toString())
                                    mapParam.put("taskType",taskType.toString())
                                    mapParam.put("taskName",edtTaskName.text.toString())
                                    mapParam.put("taskDescription",txtTaskDescription.text.toString())
                                    mapParam.put("evaluatorId",superEvaluatorId.toString())
                                    Log.e("SERVER",mapParam.toString())
                                    if(Utilities.isNetworkAvailable(this@EditTaskActivity)){
                                        showProgressBarNew()
                                        viewModel.updateTaskThroughApi(appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),mapParam,
                                            Utilities.isNetworkAvailable(this@EditTaskActivity))
                                        getCreateTaskResponse()
                                    }else{
                                        showMessage(this@EditTaskActivity,txtTaskDescription,getString(R.string.network_error))
                                    }
                                }else{
                                    showMessage(
                                        this@EditTaskActivity,
                                        superEvaluatorRecycle,
                                        getString(R.string.err_task_name)
                                    )
                                }
                            }else{
                                showMessage(
                                    this@EditTaskActivity,
                                    superEvaluatorRecycle,
                                    getString(R.string.task_description_err)
                                )
                            }
                        }

                    }else{
                        showMessage(this@EditTaskActivity,superEvaluatorRecycle,getString(R.string.please_select_super_evaluator))
                    }
                }
            }
        }
    }


    private fun getEvaluatorListData() {
        hideProgressBarNew()
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer {
            hideProgressBarNew()
            showMessage(this@EditTaskActivity, superEvaluatorRecycle, getString(it))
        })

        viewModel.createTaskViewModel.observe(
            this, androidx.lifecycle.Observer {
                hideProgressBarNew()
                if (it.success) {
                    if (it.code == 200) {
                        for (i in 0 until it.message.size) {
                            if (it.message[i].evaluatorId == superEvaluatorId) {
                                it.message[i].isClecked = true
                            }
                        }
                        if(it.message.size>7){
                            for(i in 0 until it.message.size){
                                if(i<7){
                                    superEvaluatorList.add(it.message[i])
                                    adapter.notifyDataSetChanged()
                                }else{
                                    break
                                }
                            }
                        }else {
                            superEvaluatorList.addAll(it.message)
                            adapter.notifyDataSetChanged()
                        }

                    } else {
                        showMessage(
                            this@EditTaskActivity,
                            superEvaluatorRecycle,
                            it.errorMessage.toString()
                        )
                    }
                    superEvaluatorListMore.addAll(it.message)

                } else {
                    showMessage(
                        this@EditTaskActivity,
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
            showMessage(this@EditTaskActivity, superEvaluatorRecycle, getString(it))
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
                            this@EditTaskActivity,
                            superEvaluatorRecycle,
                            it.errorMessage.toString()
                        )
                    }

                } else {
                    hideProgressBarNew()
                    showMessage(
                        this@EditTaskActivity,
                        superEvaluatorRecycle,
                        it.errorMessage.toString()
                    )
                }
            }
        )


    }

    override fun onBackPressed() {
        finish()
    }
}