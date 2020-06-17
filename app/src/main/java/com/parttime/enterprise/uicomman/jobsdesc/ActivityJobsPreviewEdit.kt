package com.parttime.enterprise.uicomman.jobsdesc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.cancan.Adapters.MyFragmentPagerAdapterEdit
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import kotlinx.android.synthetic.main.activity_job_description.*
import org.json.JSONArray
import java.util.*

import java.text.SimpleDateFormat


class ActivityJobsPreviewEdit : BaseActivity() {
    var fileList = ArrayList<String>()
    var fileList2 = ArrayList<String>()
    lateinit var adapter: MyFragmentPagerAdapterEdit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_description)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            id_back_me.rotation = 180F
        } else {
            id_back_me.rotation = 0F
        }
        getIntentContent()
    }

    private fun getIntentContent() {
        fileList = intent.getSerializableExtra("FILE_LIST") as ArrayList<String>
        adapter = MyFragmentPagerAdapterEdit(this@ActivityJobsPreviewEdit, fileList)
        viewPager.adapter = adapter
        tabIndicatorDots.setupWithViewPager(viewPager)

        intent.getSerializableExtra("PARTTIME_TYPE")
        prev_partTimeType.text = intent.getSerializableExtra("PARTTIME_TYPE").toString()
        prev_jobTitle.text = intent.getSerializableExtra("JOB_TITLE").toString()
        prev_description.text = intent.getSerializableExtra("JOB_DESC").toString()
        prev_location.text = intent.getSerializableExtra("JOB_LOCATION").toString()
        prev_branch.text = intent.getSerializableExtra("BRANCH_NAME").toString()
        prev_currencys.text = intent.getSerializableExtra("CURRENCY").toString()
        if (intent.getSerializableExtra("SPN_HOURLY_RATE").toString().contentEquals("None Of These")) {
            hourly_rates.text = intent.getSerializableExtra("EDT_HOURLY_RATE").toString()
        } else {
            hourly_rates.text = intent.getSerializableExtra("SPN_HOURLY_RATE").toString()
        }
        prev_workingHours.text = getWorkingHoursFormJon(intent.getSerializableExtra("WORKING_HOUR").toString())
        prev_totalHrPerWeek.text = getWorkingHoursFormJonTotal(intent.getSerializableExtra("WORKING_HOUR").toString())//intent.getSerializableExtra("HOURLY_WEEK_TOTAL").toString()
        prev_noOfApplicants.text = intent.getSerializableExtra("MAX_APPLICANT").toString()
        prev_workExp.text = intent.getSerializableExtra("RANGESEEKBAR").toString()
        prev_requirements.text = intent.getSerializableExtra("REQUIREMENTS").toString()
        prev_workguide.text = intent.getSerializableExtra("GUIDELINES").toString()
        prev_benefits.text = intent.getSerializableExtra("BENIFITS").toString()

        prev_map.setOnClickListener {
            //val strUri = "http://maps.google.com/maps?q=loc:"+,(Label which you want)"
            val strUri =
                "http://maps.google.com/maps?q=loc:" + intent.getSerializableExtra("LAT") + "," + intent.getSerializableExtra(
                    "LON"
                ) + " (" + "Part Time Enterprise" + ")"
            val intent = Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri))

            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")

            startActivity(intent)
        }
        id_back_me.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getWorkingHoursFormJon(json: String): String {
        var finalString = StringBuilder()
        if (json.contentEquals("1")) {
            finalString.append("NA")
        } else {
            val jsonArry = JSONArray(json)
            for (i: Int in 0 until jsonArry.length()) {
                val item = jsonArry.optJSONObject(i)
                finalString.append(
                    item.optString("startTime") + " To " + item.optString("endTime") + " (" + item.optString(
                        "day"
                    ) + ")\n"
                )
            }
        }

        return finalString.toString()
    }


    private fun getWorkingHoursFormJonTotal(json: String): String {
        if(json.contains("N")){
            return "0"
        }
        var finalString = StringBuilder()
        if (json.contentEquals("1")) {
            finalString.append("NA")
        } else {
            val jsonArry = JSONArray(json)
            for (i: Int in 0 until jsonArry.length()) {
                val item = jsonArry.optJSONObject(i)
                var startTimess=item.optString("startTime")
                var endTimess=item.optString("endTime")
                if(startTimess.trim().isNullOrEmpty() && endTimess.trim().isNullOrEmpty()){
                    return "0"
                }
                if(!item.isNull("startTime") && !item.isNull("endTime")){
                    val simpleDateFormat = SimpleDateFormat("hh:mm a")
                    //  val displayFormat = SimpleDateFormat("hh:mm a")
                    val date1 = simpleDateFormat.parse(item.optString("startTime"))
                    val date2 = simpleDateFormat.parse(item.optString("endTime"))
                    if(date1!=null && date2!=null){
                        if(Utilities.hoursDifference(date1,date2).toString().contains("-")){
                            var temp=Utilities.hoursDifference(date1,date2)
                            finalString.append( temp.toString().replace("-"," ").trim()).append(",")
                        }else{
                            finalString.append( Utilities.hoursDifference(date1,date2)).append(",")
                        }
                    }

                }
                /*finalString.append(
                    item.optString("startTime") + " To " + item.optString("endTime") + " (" + item.optString(
                        "day"
                    ) + ")\n"
                )*/
            }
        }
        var value2: String = " "
        if(!finalString.trim().isNullOrEmpty()){
            value2 = finalString.toString()
            value2 = value2.substring(0, value2.length - 1)
        }

        // total calculate Array item
        if(finalString.contains("NA")){
            return 0.toString()
        }
        var totalVal=0
        if(value2.trim().isNotEmpty()){
            var temp=value2.split(",")
            for(i in 0 until temp.size){
                totalVal += temp[i].toInt()
            }
        }
        /*if(totalVal.toString().contains("-")){
           var temp= totalVal.toString().replace("-"," ")
            totalVal=temp.trim().toInt()
        }*/
        return totalVal.toString()
    }


    override fun onBackPressed() {
        finish()
    }
}