package com.parttime.enterprise.uicomman.applicantfilter


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.*
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.prefences.AppPrefence.SharedPreferncesKeys
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.educationdetails.EducationDetails
import com.parttime.enterprise.uicomman.applicantfilter.educationlevel.EducationLevel
import com.parttime.enterprise.uicomman.applicantfilter.educationlevel.Message
import com.parttime.enterprise.uicomman.applicantfilter.nationality.NationalityResponse
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.*
import com.parttime.enterprise.uicomman.jobdetails.JobDetailsActivity
import com.parttime.enterprise.uicomman.locationaddress.ActivityLocationFilter
import com.parttime.enterprise.viewmodels.ApplicantFilterViewModel
import kotlinx.android.synthetic.main.activity_applicant_filter.*
import org.json.JSONObject


class ApplicantFilterActivity : BaseActivity(), WorkHourHistoryAdapter.OnItemClickForWorkHour,
    View.OnClickListener,
    GenderAdapter.OnItemClickGender, NewAgeAdapter.OnItemClickAge,
    LocByRadiousAdapter.OnItemClickLocByRadious,
    TaskAdapter.OnItemClickTask, EvaluationAdapter.OnItemClickEvaluation,
    EducationLevelAdapter.OnItemClickEducationLavel, RowListItemAdapter.RowItemClick,
    NationalityAdapter.OnItemClickNationality, EducationDetailsAdapter.OnItemClickEducationDetails {


    override fun rowItemClick(pos: Int) {
        if (!listRowItem.isNullOrEmpty()) {
            listRowItem.drop(pos)
            adapter1.notifyDataSetChanged()
        }
        if (listRowItem.isNullOrEmpty()) {
            recycle_Loc.visibility = View.GONE
        } else {
            recycle_Loc.visibility = View.VISIBLE
        }

    }

    var RETURN_FLAG = 1
    private var PREV_SETTING = 0
    // view Model
    lateinit var viewModel: ApplicantFilterViewModel

    lateinit var adapterWorkHistory: WorkHourHistoryAdapter
    var listWorkHistoryModel = ArrayList<WorkHistoryModel>()
    lateinit var layoutManager: LinearLayoutManager

    lateinit var adapterGender: GenderAdapter
    var listGender = ArrayList<GenderModel>()
    lateinit var layoutManagerGender: LinearLayoutManager

    /*Age Adapter Initilise*/
    lateinit var adapterAge: NewAgeAdapter
    var listAge1 = ArrayList<AgeModel>()
    lateinit var layoutManagerAge: LinearLayoutManager

    /*Loc by radious*/
    lateinit var adapterLocByRadious: LocByRadiousAdapter
    var listLocByRadious = ArrayList<LocByRadiousModel>()
    lateinit var layoutManagerLocByRadious: LinearLayoutManager

    /*Task*/
    lateinit var adapterTask: TaskAdapter
    var listTask = ArrayList<TaskModel>()
    lateinit var layoutManagerTask: LinearLayoutManager

    /*Evaluation*/
    lateinit var adapterEvaluation: EvaluationAdapter
    var listEvaluation = ArrayList<EvaluationModel>()
    lateinit var layoutManagerEvaluation: LinearLayoutManager

    /*Education Level*/
    lateinit var adapterEducationLevel: EducationLevelAdapter
    var listEducationLevel = ArrayList<Message>()
    lateinit var layoutManagerEducationLavel: LinearLayoutManager

    /*National List*/
    lateinit var adapterNationality: NationalityAdapter
    var listNationality =
        ArrayList<com.parttime.enterprise.uicomman.applicantfilter.nationality.Message>()
    lateinit var layoutManagerNationality: LinearLayoutManager
    val filterDataMap = HashMap<String, String>()


    /*Education Details*/
    lateinit var adapterEducationDetails: EducationDetailsAdapter
    var listEducationDetails =
        ArrayList<com.parttime.enterprise.uicomman.applicantfilter.educationdetails.Message>()
    lateinit var layoutManagerEduCationDetails: LinearLayoutManager

    /*Filter Data*/
    var educationLevevelList = ArrayList<Message>()
    var educationDetailsList =
        ArrayList<com.parttime.enterprise.uicomman.applicantfilter.educationdetails.Message>()
    var natilonalistList =
        ArrayList<com.parttime.enterprise.uicomman.applicantfilter.nationality.Message>()
    var locationRadiousSelectedList = ArrayList<LocByRadiousModel>()
    var evaluationsLists = ArrayList<EvaluationModel>()
    var workHistoryList = ArrayList<WorkHistoryModel>()
    var taskLists = ArrayList<TaskModel>()
    var listAges = ArrayList<AgeModel>()
    var genderSelection: String = " "
    var selectedLat = "0.0"
    var selectedLon = "0.0"
    var locFlags = 0

    var countryList = ArrayList<String>()
    var stateList = ArrayList<String>()
    var cityList = ArrayList<String>()


    // location
    private lateinit var adapter1: RowListItemAdapter
    private var listRowItem = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicant_filter)
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                ""
            ).equals("ar")
        ) {
            filterBack_filter.rotation = 180F
            ll_loc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0)
        } else {
            filterBack_filter.rotation = 0F
            ll_loc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0)
        }
        onClickInitilise()
        setAdapter()

        ll_locationSearch.setOnClickListener {
            val intent = Intent(this@ApplicantFilterActivity, ActivityLocationFilter::class.java)
            intent.putExtra("PREV_SETTING", PREV_SETTING)
            startActivityForResult(intent, 1001)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (data!!.getStringExtra("FLAG").contentEquals("2")) {
                if (listRowItem.isNotEmpty()) {
                    listRowItem.clear()
                    adapter1.notifyDataSetChanged()
                }
                PREV_SETTING = data.getIntExtra("PREV_SETTING", 0)
                selectedLat = data.getDoubleExtra("LAT", 0.0).toString()
                selectedLon = data.getDoubleExtra("LON", 0.0).toString()
                locFlags = data.getIntExtra("FLAGS", 0)

                listRowItem.addAll(data.getSerializableExtra("SELECTED") as ArrayList<String>)
                if (listRowItem.isNullOrEmpty()) {
                    listRowItem.clear()
                } else {
                    var tempList = listRowItem.distinct()
                    if (listRowItem.isNotEmpty()) {
                        listRowItem.clear()
                        adapter1.notifyDataSetChanged()
                    }
                    listRowItem.addAll(tempList)
                    if (tempList.isNotEmpty()) {
                        tempList = emptyList()
                    }
                    recycle_Loc.visibility = View.VISIBLE
                    adapter1.notifyDataSetChanged()
                }
            } else if (data.getStringExtra("FLAG").contentEquals("3")) {
                PREV_SETTING = data.getIntExtra("PREV_SETTING", 0)
                countryList = data.getSerializableExtra("COUNTRY") as ArrayList<String>
                stateList = data.getSerializableExtra("STATE") as ArrayList<String>
                cityList = data.getSerializableExtra("CITY") as ArrayList<String>
                locFlags = data.getIntExtra("FLAGS", 0)
                if (!listRowItem.isNullOrEmpty()) {
                    listRowItem.clear()
                    adapter1.notifyDataSetChanged()
                }
                listRowItem.addAll(countryList)
                listRowItem.addAll(stateList)
                listRowItem.addAll(cityList)

                if (!listRowItem.isNullOrEmpty()) {
                    var tempList = listRowItem.distinct()
                    if (listRowItem.isNotEmpty()) {
                        listRowItem.clear()
                        adapter1.notifyDataSetChanged()
                    }
                    listRowItem.addAll(tempList)
                    if (tempList.isNotEmpty()) {
                        tempList = emptyList()
                    }
                    recycle_Loc.visibility = View.VISIBLE
                    adapter1.notifyDataSetChanged()
                }
            }
        }
    }


    /* Education Level */
    override fun onClickEducationLavel(value1: ArrayList<Message>) {
        if (educationLevevelList.isNotEmpty()) {
            educationLevevelList.clear()
        }
        // check which is clickable
        for (j in 0 until value1.size) {
            if (value1[j].isClicked) {
                educationLevevelList.add(value1[j])
            }
        }
        //educationLevevelList = value1
        // showMessage(this@ApplicantFilterActivity, recyleWorkingHour, value1.toString())
    }

    /*Selected Gender Get*/
    override fun onClickGender(value: GenderModel) {
        // showMessage(this@ApplicantFilterActivity, recyleWorkingHour, value.toString())
        genderSelection = value.name.trim()
    }


    /*Age Selected Value*/
    override fun onClickAge(value: ArrayList<AgeModel>) {
        //showMessage(this@ApplicantFilterActivity, recyleWorkingHour, value.toString())
        if (listAges.isNotEmpty()) {
            listAges.clear()
        }
        // check which is clickable
        for (j in 0 until value.size) {
            if (value[j].isClick) {
                listAges.add(value[j])
            }
        }
    }

    /*LOC BY RADIOUS SELECTED*/
    override fun onClickLocByRadious(value: ArrayList<LocByRadiousModel>) {
        //showMessage(this@ApplicantFilterActivity, recyleWorkingHour, value.toString())
        if (locationRadiousSelectedList.isNotEmpty()) {
            locationRadiousSelectedList.clear()
        }
        // check which is clickable
        for (j in 0 until value.size) {
            if (value[j].isClick) {
                locationRadiousSelectedList.add(value[j])
            }
        }
        // locationRadiousSelectedList = value
    }

    /*Task Selection*/
    override fun onClickTask(value: ArrayList<TaskModel>) {
        //showMessage(this@ApplicantFilterActivity, recyleWorkingHour, value.toString())
        if (taskLists.isNotEmpty()) {
            taskLists.clear()
        }
        // check which is clickable
        for (j in 0 until value.size) {
            if (value[j].isClick) {
                taskLists.add(value[j])
            }
        }
    }

    /*Evaluation Selection*/
    override fun onClickEvaluation(value: ArrayList<EvaluationModel>) {
        //showMessage(this@ApplicantFilterActivity, recyleWorkingHour, value.toString())
        if (evaluationsLists.isNotEmpty()) {
            evaluationsLists.clear()
        }
        // check which is clickable
        for (j in 0 until value.size) {
            if (value[j].isClick) {
                evaluationsLists.add(value[j])
            }
        }
    }


    /*Selected Working Hour Data */
    override fun onClickWorkHour(value: ArrayList<WorkHistoryModel>) {
        // showMessage(this@ApplicantFilterActivity, recyleWorkingHour, value.toString())
        if (workHistoryList.isNotEmpty()) {
            workHistoryList.clear()
        }
        // check which is clickable
        for (j in 0 until value.size) {
            if (value[j].isClick) {
                workHistoryList.add(value[j])
            }
        }
    }

    /*Select National List*/
    override fun onClickNationality(value: ArrayList<com.parttime.enterprise.uicomman.applicantfilter.nationality.Message>) {
        // showMessage(this@ApplicantFilterActivity, recyleWorkingHour, value.toString())
        if (natilonalistList.isNotEmpty()) {
            natilonalistList.clear()
        }
        // check which is clickable
        for (j in 0 until value.size) {
            if (value[j].isClicked) {
                natilonalistList.add(value[j])
            }
        }
        // natilonalistList = value
    }

    /*select education list items*/
    override fun onClickEducationDetails(value: ArrayList<com.parttime.enterprise.uicomman.applicantfilter.educationdetails.Message>) {
        // showMessage(this@ApplicantFilterActivity, recyleWorkingHour, value.toString())
        if (educationDetailsList.isNotEmpty()) {
            educationDetailsList.clear()
        }
        // check which is clickable
        for (j in 0 until value.size) {
            if (value[j].isClicked) {
                educationDetailsList.add(value[j])
            }
        }
        // educationDetailsList = value
    }


    /*Onclick Initialise*/
    private fun onClickInitilise() {
        ll_workhourView.setOnClickListener(this@ApplicantFilterActivity)
        ll_viewGender.setOnClickListener(this@ApplicantFilterActivity)
        ll_ageView.setOnClickListener(this@ApplicantFilterActivity)
        ll_locByRadView.setOnClickListener(this@ApplicantFilterActivity)
        ll_taskView.setOnClickListener(this@ApplicantFilterActivity)
        ll_evaluationView.setOnClickListener(this@ApplicantFilterActivity)
        ll_edecationLebvelView.setOnClickListener(this@ApplicantFilterActivity)
        ll_nationalityView.setOnClickListener(this@ApplicantFilterActivity)
        jobAddTitle_txt.setOnClickListener(this@ApplicantFilterActivity)
        btnLogin_filter.setOnClickListener(this@ApplicantFilterActivity)
        filterBack_filter.setOnClickListener(this@ApplicantFilterActivity)
        // check if prev location selected is it or not
        if (listRowItem.isNotEmpty()) {
            adapter1.notifyDataSetChanged()
        }
    }

    /*Onclick functionality*/
    override fun onClick(v: View?) {
        when (v) {
            filterBack_filter -> {
                onBackPressed()
            }
            jobAddTitle_txt -> {
                PREV_SETTING = 0
                activity.appPrefence.setString(
                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                    " "
                )
                if (!filterDataMap.isNullOrEmpty())
                    filterDataMap.clear()
                if (!countryList.isNullOrEmpty())
                    countryList.clear()
                if (!stateList.isNullOrEmpty())
                    stateList.clear()
                if (!cityList.isNullOrEmpty())
                    cityList.clear()
                if (!listRowItem.isNullOrEmpty()) {
                    listRowItem.clear()
                    adapter1.notifyDataSetChanged()
                }

                recreate()
            }
            ll_workhourView -> {
                if (workhourView.isVisible) {
                    ll_workhourView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_down,
                        0
                    )
                    workhourView.visibility = View.GONE
                    recyleWorkingHour.visibility = View.GONE
                } else {
                    ll_workhourView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_up,
                        0
                    )
                    workhourView.visibility = View.VISIBLE
                    recyleWorkingHour.visibility = View.VISIBLE
                }
            }
            ll_viewGender -> {
                if (viewGender.isVisible) {
                    ll_viewGender.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_down,
                        0
                    )
                    viewGender.visibility = View.GONE
                    recyleGender.visibility = View.GONE
                } else {
                    ll_viewGender.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_up,
                        0
                    )
                    viewGender.visibility = View.VISIBLE
                    recyleGender.visibility = View.VISIBLE
                }
            }
            ll_ageView -> {
                if (ageView.isVisible) {
                    ll_ageView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_down,
                        0
                    )
                    ageView.visibility = View.GONE
                    recyleAge.visibility = View.GONE
                } else {
                    ll_ageView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_up, 0)
                    ageView.visibility = View.VISIBLE
                    recyleAge.visibility = View.VISIBLE
                }
            }
            ll_locByRadView -> {
                if (locByRadView.isVisible) {
                    ll_locByRadView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_down,
                        0
                    )
                    locByRadView.visibility = View.GONE
                    recyleLocRadius.visibility = View.GONE
                } else {
                    ll_locByRadView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_up,
                        0
                    )
                    locByRadView.visibility = View.VISIBLE
                    recyleLocRadius.visibility = View.VISIBLE
                }
            }
            ll_taskView -> {
                if (taskView.isVisible) {
                    ll_taskView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_down,
                        0
                    )
                    taskView.visibility = View.GONE
                    recyleTask.visibility = View.GONE
                } else {
                    ll_taskView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_up, 0)
                    taskView.visibility = View.VISIBLE
                    recyleTask.visibility = View.VISIBLE
                }
            }

            ll_evaluationView -> {
                if (evaluationView.isVisible) {
                    ll_evaluationView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_down,
                        0
                    )
                    evaluationView.visibility = View.GONE
                    recyleEvaluation.visibility = View.GONE
                } else {
                    ll_evaluationView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_up,
                        0
                    )
                    evaluationView.visibility = View.VISIBLE
                    recyleEvaluation.visibility = View.VISIBLE
                }
            }
            ll_edecationLebvelView -> {
                if (edecationLebvelView.isVisible) {
                    ll_edecationLebvelView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_down,
                        0
                    )
                    edecationLebvelView.visibility = View.GONE
                    recyleEducationLevel.visibility = View.GONE
                    recyleEducationDetails.visibility = View.GONE
                    edecationDetailsView.visibility = View.GONE
                    ll_edecationDetailsView.visibility = View.GONE
                } else {
                    ll_edecationLebvelView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_up,
                        0
                    )
                    edecationLebvelView.visibility = View.VISIBLE
                    recyleEducationLevel.visibility = View.VISIBLE
                    recyleEducationDetails.visibility = View.VISIBLE
                    edecationDetailsView.visibility = View.VISIBLE
                    ll_edecationDetailsView.visibility = View.VISIBLE
                }
            }
            btnLogin_filter -> {
                if (!educationLevevelList.isNullOrEmpty()) {
                    var value: String = " "
                    val sb = StringBuilder()
                    for (i in 0 until educationLevevelList.size) {
                        sb.append(educationLevevelList[i].educationId).append(",")
                    }
                    value = sb.toString()
                    value = value.substring(0, value.length - 1)
                    filterDataMap["education"] = value
                }
                if (!educationDetailsList.isNullOrEmpty()) {
                    var value: String = " "
                    val sb = StringBuilder()
                    for (i in 0 until educationDetailsList.size) {
                        sb.append(educationDetailsList[i].educationDetailId).append(",")
                    }
                    value = sb.toString()
                    value = value.substring(0, value.length - 1)
                    filterDataMap["educationDetail"] = value
                }
                if (!natilonalistList.isNullOrEmpty()) {
                    var value: String = " "
                    val sb = StringBuilder()
                    for (i in 0 until natilonalistList.size) {
                        sb.append(natilonalistList[i].nationality).append(",")
                    }
                    value = sb.toString()
                    value = value.substring(0, value.length - 1)
                    filterDataMap["nationality"] = value
                }

                if (!listAges.isNullOrEmpty()) {
                    var value: String = " "
                    val sb = StringBuilder()
                    for (i in 0 until listAges.size) {
                        if (listAges[i].isClick) {
                            sb.append(listAges[i].name).append(",")
                        }
                    }
                    if (sb.length > 2) {
                        value = sb.toString()
                        value = value.substring(0, value.length - 1)
                        filterDataMap["age"] = value
                    }
                }
                // task list
                if (!taskLists.isNullOrEmpty()) {
                    var value: String = " "
                    val sb = StringBuilder()
                    for (i in 0 until taskLists.size) {
                        if (taskLists[i].isClick) {
                            sb.append(taskLists[i].name).append(",")
                        }
                    }
                    if (sb.length > 2) {
                        value = sb.toString()
                        value = value.substring(0, value.length - 1)
                        filterDataMap["task"] = value
                    }
                }
                // evaluation List

                if (!evaluationsLists.isNullOrEmpty()) {
                    var value: String = " "
                    val sb = StringBuilder()
                    for (i in 0 until evaluationsLists.size) {
                        if (evaluationsLists[i].isClick) {
                            sb.append(evaluationsLists[i].name).append(",")
                        }
                    }
                    if (sb.length > 2) {
                        value = sb.toString()
                        value = value.substring(0, value.length - 1)
                        filterDataMap["evaluation"] = value
                    }
                }
                // workHistoryList

                if (!workHistoryList.isNullOrEmpty()) {
                    var value: String = " "
                    val sb = StringBuilder()
                    for (i in 0 until workHistoryList.size) {
                        if (workHistoryList[i].isClick) {
                            sb.append(workHistoryList[i].name).append(",")
                        }
                    }
                    if (sb.length > 2) {
                        value = sb.toString()
                        value = value.substring(0, value.length - 1)
                        filterDataMap["workHistory"] = value
                    }
                }
                if (!locationRadiousSelectedList.isNullOrEmpty()) {
                    var value: String = " "
                    val sb = StringBuilder()
                    for (i in 0 until locationRadiousSelectedList.size) {
                        if (locationRadiousSelectedList[i].name.contains("With in 5 KM") && locationRadiousSelectedList[i].isClick) {
                            sb.append("5").append(",")
                        } else if (locationRadiousSelectedList[i].name.contains("Up to 10 KM") && locationRadiousSelectedList[i].isClick) {
                            sb.append("10").append(",")
                        } else if (locationRadiousSelectedList[i].name.contains("11-15 KM") && locationRadiousSelectedList[i].isClick) {
                            sb.append("15").append(",")
                        } else if (locationRadiousSelectedList[i].name.contains("20 KM +") && locationRadiousSelectedList[i].isClick) {
                            sb.append("20").append(",")
                        }
                    }
                    value = sb.toString()
                    value = value.substring(0, value.length - 1)
                    filterDataMap["radius"] = value
                }
                // set gender
                if (genderSelection.trim().isNotEmpty()) {
                    filterDataMap["gender"] = genderSelection.trim()
                }

                if (locFlags == 2) {
                    filterDataMap.put("desiredLat", selectedLat)
                    filterDataMap.put("desiredLong", selectedLon)
                } else if (locFlags == 3) {
                    if (!countryList.isNullOrEmpty() && !stateList.isNullOrEmpty() && !cityList.isNullOrEmpty()) {

                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until stateList.size) {
                            sb.append(stateList[i]).append(",")
                        }
                        value = sb.toString()
                        value = value.substring(0, value.length - 1)
                        filterDataMap["state"] = value

                        var value2: String = " "
                        val sb2 = StringBuilder()
                        for (i in 0 until countryList.size) {
                            sb2.append(countryList[i]).append(",")
                        }
                        value2 = sb2.toString()
                        value2 = value2.substring(0, value2.length - 1)
                        filterDataMap["country"] = value2

                        var value1: String = " "
                        val sb1 = StringBuilder()
                        for (i in 0 until cityList.size) {
                            sb1.append(cityList[i]).append(",")
                        }
                        value1 = sb1.toString()
                        value1 = value1.substring(0, value1.length - 1)
                        filterDataMap["city"] = value1

                    }

                }
                // For Activity Redirect
                if (!filterDataMap.isNullOrEmpty()) {
                    // for prefence through logic filter
                    val gson = Gson()
                    val filterHashMapString = gson.toJson(filterDataMap)
                    activity.appPrefence.setString(
                        SharedPreferncesKeys.FILTER.toString(),
                        filterHashMapString
                    )
                    val intent =
                        Intent(this@ApplicantFilterActivity, JobDetailsActivity::class.java)
                    intent.putExtra("FLAG", 2)
                    intent.putExtra("KEY_API", filterDataMap)
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    showMessage(
                        this@ApplicantFilterActivity,
                        nationalityView,
                        getString(R.string.filter_message)
                    )
                }


            }
            ll_nationalityView -> {
                if (nationalityView.isVisible) {
                    ll_nationalityView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_down,
                        0
                    )
                    nationalityView.visibility = View.GONE
                    recyleNationality.visibility = View.GONE
                } else {
                    ll_nationalityView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.drop_up,
                        0
                    )
                    nationalityView.visibility = View.VISIBLE
                    recyleNationality.visibility = View.VISIBLE
                }
            }
        }
    }


    /*  Method to set recyclerview adapter
        */
    private fun setAdapter() {

        // work History value Set
        var arr = resources.getStringArray(R.array.working_hour_history)
        for (i in 0 until arr.size) {
            listWorkHistoryModel.add(WorkHistoryModel(arr[i], false))
        }
        // prev selection check
        if (activity.appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                " "
            ).toString().trim().length > 2
        ) {
            val storedHashMapString =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                    " "
                )
            if (storedHashMapString.trim().isNotEmpty()) {
                if (!activity.getHashMapFromSharePrepence(
                        storedHashMapString,
                        Gson()
                    ).isNullOrEmpty()
                ) {
                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                    val educationJsonObject = JSONObject(storedHashMapString)
                    if (educationJsonObject.has("workHistory")) {
                        var edutionIds = educationJsonObject.optString("workHistory")
                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                            var eduId = edutionIds.split(",")
                            for (i in 0 until listWorkHistoryModel.size) {
                                if (eduId.contains(listWorkHistoryModel[i].name.toString())) {
                                    listWorkHistoryModel[i].isClick = true
                                    workHistoryList.add(listWorkHistoryModel[i])

                                }
                            }
                            recyleWorkingHour.visibility = View.VISIBLE

                        }

                    }
                }
            }
        }

        adapterWorkHistory =
            WorkHourHistoryAdapter(activity, listWorkHistoryModel, this@ApplicantFilterActivity)
        recyleWorkingHour.adapter = adapterWorkHistory
        layoutManager = LinearLayoutManager(activity)
        recyleWorkingHour.layoutManager = layoutManager


        /* Age value Set*/
        var arrAge = resources.getStringArray(R.array.age_array)
        for (i in 0 until arrAge.size) {
            listAge1.add(AgeModel(arrAge[i], false))
        }

        // prev selection check
        if (activity.appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                " "
            ).toString().trim().length > 2
        ) {
            val storedHashMapString =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                    " "
                )
            if (storedHashMapString.trim().isNotEmpty()) {
                if (!activity.getHashMapFromSharePrepence(
                        storedHashMapString,
                        Gson()
                    ).isNullOrEmpty()
                ) {
                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                    val educationJsonObject = JSONObject(storedHashMapString)
                    if (educationJsonObject.has("age")) {
                        var edutionIds = educationJsonObject.optString("age")
                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                            var eduId = edutionIds.split(",")
                            for (i in 0 until listAge1.size) {
                                if (eduId.contains(listAge1[i].name.toString())) {
                                    listAge1[i].isClick = true
                                    listAges.add(listAge1[i])

                                }
                            }
                            recyleAge.visibility = View.VISIBLE

                        }

                    }
                }
            }
        }

        adapterAge = NewAgeAdapter(activity, listAge1, this@ApplicantFilterActivity)
        recyleAge.adapter = adapterAge
        layoutManagerAge = LinearLayoutManager(activity)
        recyleAge.layoutManager = layoutManagerAge

        /* LocBy Radious value Set*/
        var arrLocByRadious = resources.getStringArray(R.array.location_by_radious)
        for (i in 0 until arrLocByRadious.size) {
            listLocByRadious.add(LocByRadiousModel(arrLocByRadious[i], false))
        }

        // prev selection check
        if (activity.appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                " "
            ).toString().trim().length > 2
        ) {
            val storedHashMapString =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                    " "
                )
            if (storedHashMapString.trim().isNotEmpty()) {
                if (!activity.getHashMapFromSharePrepence(
                        storedHashMapString,
                        Gson()
                    ).isNullOrEmpty()
                ) {
                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                    val educationJsonObject = JSONObject(storedHashMapString)
                    if (educationJsonObject.has("radius")) {
                        var edutionIds = educationJsonObject.optString("radius")
                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                            var eduId = edutionIds.split(",")
                            for (i in 0 until eduId.size) {
                                if (eduId[i].contains("5")) {
                                    listLocByRadious[0].isClick = true
                                    locationRadiousSelectedList.add(listLocByRadious[0])
                                } else if (eduId[i].contains("10")) {
                                    listLocByRadious[1].isClick = true
                                    locationRadiousSelectedList.add(listLocByRadious[1])
                                } else if (eduId[i].contains("15")) {
                                    listLocByRadious[2].isClick = true
                                    locationRadiousSelectedList.add(listLocByRadious[2])
                                } else if (eduId[i].contains("20")) {
                                    listLocByRadious[3].isClick = true
                                    locationRadiousSelectedList.add(listLocByRadious[3])
                                }
                            }
                            recyleLocRadius.visibility = View.VISIBLE

                        }

                    }
                }
            }
        }

        adapterLocByRadious =
            LocByRadiousAdapter(activity, listLocByRadious, this@ApplicantFilterActivity)
        recyleLocRadius.adapter = adapterLocByRadious
        layoutManagerLocByRadious = LinearLayoutManager(activity)
        recyleLocRadius.layoutManager = layoutManagerLocByRadious

        /* Task value  Set*/
        var arrTask = resources.getStringArray(R.array.task)
        for (i in 0 until arrTask.size) {
            listTask.add(TaskModel(arrTask[i], false))
        }
        // prev selection check
        if (activity.appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                " "
            ).toString().trim().length > 2
        ) {
            val storedHashMapString =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                    " "
                )
            if (storedHashMapString.trim().isNotEmpty()) {
                if (!activity.getHashMapFromSharePrepence(
                        storedHashMapString,
                        Gson()
                    ).isNullOrEmpty()
                ) {
                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                    val educationJsonObject = JSONObject(storedHashMapString)
                    if (educationJsonObject.has("task")) {
                        var edutionIds = educationJsonObject.optString("task")
                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                            var eduId = edutionIds.split(",")
                            for (i in 0 until listTask.size) {
                                if (eduId.contains(listTask[i].name.toString())) {
                                    listTask[i].isClick = true
                                    taskLists.add(listTask[i])

                                }
                            }
                            recyleTask.visibility = View.VISIBLE

                        }

                    }
                }
            }
        }
        adapterTask = TaskAdapter(activity, listTask, this@ApplicantFilterActivity)
        recyleTask.adapter = adapterTask
        layoutManagerTask = LinearLayoutManager(activity)
        recyleTask.layoutManager = layoutManagerTask

        /*Evaluation Value Set*/

        var arrEvaluationModel = resources.getStringArray(R.array.evolution)
        for (i in 0 until arrEvaluationModel.size) {
            listEvaluation.add(EvaluationModel(arrEvaluationModel[i], false))
        }

        // prev selection check
        if (activity.appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                " "
            ).toString().trim().length > 2
        ) {
            val storedHashMapString =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                    " "
                )
            if (storedHashMapString.trim().isNotEmpty()) {
                if (!activity.getHashMapFromSharePrepence(
                        storedHashMapString,
                        Gson()
                    ).isNullOrEmpty()
                ) {
                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                    val educationJsonObject = JSONObject(storedHashMapString)
                    if (educationJsonObject.has("evaluation")) {
                        var edutionIds = educationJsonObject.optString("evaluation")
                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                            var eduId = edutionIds.split(",")
                            for (i in 0 until listEvaluation.size) {
                                if (eduId.contains(listEvaluation[i].name.toString())) {
                                    listEvaluation[i].isClick = true
                                    evaluationsLists.add(listEvaluation[i])

                                }
                            }
                            recyleEvaluation.visibility = View.VISIBLE

                        }

                    }
                }
            }
        }
        adapterEvaluation =
            EvaluationAdapter(activity, listEvaluation, this@ApplicantFilterActivity)
        recyleEvaluation.adapter = adapterEvaluation
        layoutManagerEvaluation = LinearLayoutManager(activity)
        recyleEvaluation.layoutManager = layoutManagerEvaluation


        // gender value set
        var arrGender = resources.getStringArray(R.array.gender_array)
        for (i in 0 until arrGender.size) {
            listGender.add(GenderModel(arrGender[i], false,"0"))
        }

        // prev selection check
        if (activity.appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                " "
            ).toString().trim().length > 2
        ) {
            val storedHashMapString =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                    " "
                )
            if (storedHashMapString.trim().isNotEmpty()) {
                if (!activity.getHashMapFromSharePrepence(
                        storedHashMapString,
                        Gson()
                    ).isNullOrEmpty()
                ) {
                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                    val educationJsonObject = JSONObject(storedHashMapString)
                    if (educationJsonObject.has("gender")) {
                        var edutionIds = educationJsonObject.optString("gender")
                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                            var eduId = edutionIds.split(",")
                            for (i in 0 until listGender.size) {
                                if (eduId.contains(listGender[i].name.toString())) {
                                    listGender[i].isClick = true
                                    genderSelection = listGender[i].name

                                }
                            }
                            recyleGender.visibility = View.VISIBLE

                        }

                    }
                }
            }
        }

        adapterGender = GenderAdapter(activity, listGender, this@ApplicantFilterActivity)
        recyleGender.adapter = adapterGender
        layoutManagerGender = LinearLayoutManager(activity)
        recyleGender.layoutManager = layoutManagerGender

        // loc multiple view add adapter
        adapter1 = RowListItemAdapter(
            this@ApplicantFilterActivity,
            listRowItem,
            this@ApplicantFilterActivity
        )
        recycle_Loc.layoutManager = GridLayoutManager(this@ApplicantFilterActivity, 2)
        recycle_Loc.adapter = adapter1
        recycle_Loc.visibility = View.VISIBLE


        // call from api and set
        getPaEducationLevel()
        getNationality()


        // prev selection check
        if (activity.appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                " "
            ).toString().trim().length > 2
        ) {
            val storedHashMapString =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                    " "
                )
            if (storedHashMapString.trim().isNotEmpty()) {
                if (!activity.getHashMapFromSharePrepence(
                        storedHashMapString,
                        Gson()
                    ).isNullOrEmpty()
                ) {
                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                    val educationJsonObject = JSONObject(storedHashMapString)
                    //recycle_Loc.visibility=View.VISIBLE
                    if (educationJsonObject.has("country")) {
                        var edutionIds = educationJsonObject.optString("country")
                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                            var eduId = edutionIds.split(",")
                            for (i in 0 until eduId.size) {
                                // if (eduId.contains(listRowItem[i])) {
                                listRowItem.add(eduId[i])
                                countryList.add(eduId[i])
                                // }
                            }
                            recycle_Loc.visibility = View.VISIBLE
                            adapter1.notifyDataSetChanged()

                        }

                    }
                    if (educationJsonObject.has("city")) {
                        var edutionIds = educationJsonObject.optString("city")
                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                            var eduId = edutionIds.split(",")
                            for (i in 0 until eduId.size) {
                                // if (eduId.contains(listRowItem[i])) {
                                listRowItem.add(eduId[i])
                                cityList.add(eduId[i])
                                // }
                            }
                            recycle_Loc.visibility = View.VISIBLE
                            adapter1.notifyDataSetChanged()

                        }
                    }
                    if (educationJsonObject.has("state")) {
                        var edutionIds = educationJsonObject.optString("state")
                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                            var eduId = edutionIds.split(",")
                            for (i in 0 until eduId.size) {
                                // if (eduId.contains(listRowItem[i])) {
                                listRowItem.add(eduId[i])
                                stateList.add(eduId[i])
                                //}
                            }
                            recycle_Loc.visibility = View.VISIBLE
                            adapter1.notifyDataSetChanged()

                        }

                    }
                }
            }
        }


    }


    /*Get education level list From Api*/
    fun getPaEducationLevel() {
        viewModel = ViewModelProviders.of(this@ApplicantFilterActivity)
            .get(ApplicantFilterViewModel::class.java)
        viewModel.getEducationList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            showMessage(this@ApplicantFilterActivity, recyleEducationLevel, getString(msg))
        })

        viewModel.educationLevelResponse.observe(
            this,
            androidx.lifecycle.Observer { response: EducationLevel ->
                if (response.success) {
                    if (response.code == 200) {
                        // Success Response
                        listEducationLevel = response.message
                        // prev selection check
                        if (activity.appPrefence.getString(
                                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                                " "
                            ).toString().trim().length > 2
                        ) {
                            val storedHashMapString =
                                activity.appPrefence.getString(
                                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                                    " "
                                )
                            if (storedHashMapString.trim().isNotEmpty()) {
                                if (!activity.getHashMapFromSharePrepence(
                                        storedHashMapString,
                                        Gson()
                                    ).isNullOrEmpty()
                                ) {
                                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                                    val educationJsonObject = JSONObject(storedHashMapString)
                                    if (educationJsonObject.has("education")) {
                                        var edutionIds = educationJsonObject.optString("education")
                                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                                            var eduId = edutionIds.split(",")
                                            for (i in 0 until listEducationLevel.size) {
                                                if (eduId.contains(listEducationLevel[i].educationId.toString())) {
                                                    listEducationLevel[i].isClicked = true
                                                    educationLevevelList.add(listEducationLevel[i])
                                                }
                                            }

                                            recyleEducationLevel.visibility = View.VISIBLE
                                        }

                                    }
                                }
                            }
                        }
                        /*Api through get Education Level Adapter*/
                        adapterEducationLevel =
                            EducationLevelAdapter(
                                activity,
                                listEducationLevel,
                                this@ApplicantFilterActivity
                            )
                        recyleEducationLevel.adapter = adapterEducationLevel
                        layoutManagerEducationLavel = LinearLayoutManager(activity)
                        recyleEducationLevel.layoutManager = layoutManagerEducationLavel
                        getEducationDetailList()

                    } else if (response.code == 204) {
                        showMessage(
                            this@ApplicantFilterActivity,
                            recyleEducationLevel,
                            response.errorMessage.toString()
                        )
                    } else if (response.code == 401) {
                        showMessage(
                            this@ApplicantFilterActivity,
                            recyleEducationLevel,
                            response.errorMessage.toString()
                        )
                    }
                } else {
                    showMessage(
                        this@ApplicantFilterActivity,
                        recyleEducationLevel,
                        response.errorMessage.toString()
                    )
                }
            })
    }


    /*Get education details list From Api*/
    fun getEducationDetailList() {
        viewModel = ViewModelProviders.of(this@ApplicantFilterActivity)
            .get(ApplicantFilterViewModel::class.java)
        viewModel.getEducationDetailList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            showMessage(this@ApplicantFilterActivity, recyleEducationLevel, getString(msg))
        })

        viewModel.educationDetailsListResponse.observe(
            this,
            androidx.lifecycle.Observer { response: EducationDetails ->
                if (response.success) {
                    if (response.code == 200) {
                        // Success Response
                        listEducationDetails = response.message
                        // prev selection check
                        if (activity.appPrefence.getString(
                                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                                " "
                            ).toString().trim().length > 2
                        ) {
                            val storedHashMapString =
                                activity.appPrefence.getString(
                                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                                    " "
                                )
                            if (storedHashMapString.trim().isNotEmpty()) {
                                if (!activity.getHashMapFromSharePrepence(
                                        storedHashMapString,
                                        Gson()
                                    ).isNullOrEmpty()
                                ) {
                                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                                    val educationJsonObject = JSONObject(storedHashMapString)
                                    if (educationJsonObject.has("educationDetail")) {
                                        var edutionIds =
                                            educationJsonObject.optString("educationDetail")
                                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                                            var eduId = edutionIds.split(",")
                                            for (i in 0 until listEducationDetails.size) {
                                                if (eduId.contains(listEducationDetails[i].educationDetailId.toString())) {
                                                    listEducationDetails[i].isClicked = true
                                                    educationDetailsList.add(listEducationDetails[i])
                                                }
                                            }
                                            recyleEducationDetails.visibility = View.VISIBLE
                                            ll_edecationDetailsView.visibility = View.VISIBLE
                                            edecationDetailsView.visibility = View.VISIBLE
                                        }

                                    }
                                }
                            }
                        }
                        /*Api through get Education Level Adapter*/
                        adapterEducationDetails =
                            EducationDetailsAdapter(
                                activity,
                                listEducationDetails,
                                this@ApplicantFilterActivity
                            )
                        recyleEducationDetails.adapter = adapterEducationDetails
                        layoutManagerEduCationDetails = LinearLayoutManager(activity)
                        recyleEducationDetails.layoutManager = layoutManagerEduCationDetails

                    } else if (response.code == 204) {
                        showMessage(
                            this@ApplicantFilterActivity,
                            recyleEducationLevel,
                            response.errorMessage.toString()
                        )
                    } else if (response.code == 401) {
                        showMessage(
                            this@ApplicantFilterActivity,
                            recyleEducationLevel,
                            response.errorMessage.toString()
                        )
                    }
                } else {
                    showMessage(
                        this@ApplicantFilterActivity,
                        recyleEducationLevel,
                        response.errorMessage.toString()
                    )
                }
            })
    }

    /*Get education level list From Api*/
    fun getNationality() {
        viewModel = ViewModelProviders.of(this@ApplicantFilterActivity)
            .get(ApplicantFilterViewModel::class.java)
        viewModel.getNationality(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            showMessage(this@ApplicantFilterActivity, recyleEducationLevel, getString(msg))
        })

        viewModel.nationalLisResponse.observe(
            this,
            androidx.lifecycle.Observer { response: NationalityResponse ->
                if (response.success) {
                    if (response.code == 200) {
                        // Success Response
                        listNationality = response.message

                        // prev selection check
                        if (activity.appPrefence.getString(
                                AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                                " "
                            ).toString().trim().length > 2
                        ) {
                            val storedHashMapString =
                                activity.appPrefence.getString(
                                    AppPrefence.SharedPreferncesKeys.FILTER.toString(),
                                    " "
                                )
                            if (storedHashMapString.trim().isNotEmpty()) {
                                if (!activity.getHashMapFromSharePrepence(
                                        storedHashMapString,
                                        Gson()
                                    ).isNullOrEmpty()
                                ) {
                                    //hashMap.putAll(activity.getHashMapFromSharePrepence(storedHashMapString, Gson()))
                                    //{"education":"1,3","nationality":"1,2","gender":"Female","radius":"5,10","age":"Any of the following option,16-19,20-25,26-35,40-60"}
                                    val educationJsonObject = JSONObject(storedHashMapString)
                                    if (educationJsonObject.has("nationality")) {
                                        var edutionIds =
                                            educationJsonObject.optString("nationality")
                                        if (!edutionIds.isNullOrBlank() && !edutionIds.isNullOrEmpty()) {
                                            var eduId = edutionIds.split(",")
                                            for (i in 0 until listNationality.size) {
                                                if (eduId.contains(listNationality[i].nationality.toString())) {
                                                    listNationality[i].isClicked = true
                                                    natilonalistList.add(listNationality[i])
                                                }
                                            }
                                            recyleNationality.visibility = View.VISIBLE
                                        }

                                    }
                                }
                            }
                        }


                        /*Api through get Education Level Adapter*/
                        adapterNationality = NationalityAdapter(
                            activity,
                            listNationality,
                            this@ApplicantFilterActivity
                        )
                        recyleNationality.adapter = adapterNationality
                        layoutManagerNationality = LinearLayoutManager(activity)
                        recyleNationality.layoutManager = layoutManagerNationality

                    } else if (response.code == 204) {
                        showMessage(
                            this@ApplicantFilterActivity,
                            recyleEducationLevel,
                            response.errorMessage.toString()
                        )
                    } else if (response.code == 401) {
                        showMessage(
                            this@ApplicantFilterActivity,
                            recyleEducationLevel,
                            response.errorMessage.toString()
                        )
                    }
                } else {
                    showMessage(
                        this@ApplicantFilterActivity,
                        recyleEducationLevel,
                        response.errorMessage.toString()
                    )
                }
            })
    }

    override fun onBackPressed() {
        val intent = Intent(this@ApplicantFilterActivity, JobDetailsActivity::class.java)
        intent.putExtra("FLAG", RETURN_FLAG)
        setResult(RESULT_OK, intent)
        finish()

    }
}