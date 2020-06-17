package com.parttime.enterprise.uicomman.enrollworkerprofile.enrollmores


import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.MoreMonthAdapter
import com.parttime.enterprise.helpers.GridSpacingItemDecoration
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.AgeModel
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.ActivityMonthDetails
import kotlinx.android.synthetic.main.acivity_more.*
import java.util.*
import kotlin.collections.ArrayList


class EnrollMoreActivity : BaseActivity(), View.OnClickListener, MoreMonthAdapter.OnItemClickMonth {


    private var yearList = ArrayList<String>()
    lateinit var adapterMonth: MoreMonthAdapter
    var listAge1 = ArrayList<AgeModel>()
    lateinit var layoutManagerMonth: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acivity_more)
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                ""
            ).equals("ar")
        ) {
            moreBack.rotation = 180F
        } else {
            moreBack.rotation = 0F
        }
        inItViews()
        inItAdapter()
    }

    private fun inItAdapter() {
        // initilise with current Year
        var months = resources.getStringArray(R.array.years)
        for (i in 0 until months.size) {
            listAge1.add(AgeModel(months[i] + "-" + spnToolBarYear.selectedItem.toString(), false))
        }
        adapterMonth = MoreMonthAdapter(activity, listAge1, this@EnrollMoreActivity)
        monthRecycleView.adapter = adapterMonth
        layoutManagerMonth = GridLayoutManager(activity, 3)
        monthRecycleView.addItemDecoration(GridSpacingItemDecoration(3,dpToPx(2),true))
        monthRecycleView.layoutManager = layoutManagerMonth
    }

    override fun onClickMonth(value: AgeModel) {
        //showToast(value.toString())
        var intenti= Intent(this@EnrollMoreActivity,ActivityMonthDetails::class.java)
        var s= intent.getStringExtra("USERID")
        intenti.putExtra("USERID",s)
        intenti.putExtra("MM",value.name)
        startActivity(intenti)
    }

    private fun inItViews() {
        moreBack.setOnClickListener(this)
        initYearList()
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }

    private fun initYearList() {
        // first get Current Year
        val today = Date() // Fri Jun 17 14:54:28 PDT 2016
        val cal = Calendar.getInstance()
        cal.time = today // don't forget this if date is arbitrary e.g. 01-01-2014
        var year = cal.get(Calendar.YEAR) // 2016
        // set Year current to minus five
        yearList.add(year.toString())
        for (i in 0..3) {
            year = year.dec()
            yearList.add(year.toString())
        }
        // set in to year
        spnToolBarYear.adapter = ArrayAdapter<String>(
            this@EnrollMoreActivity,
            android.R.layout.simple_spinner_dropdown_item,
            yearList
        )
        // spinner Item click listner
        spnToolBarYear?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!listAge1.isNullOrEmpty()) {
                    listAge1.clear()
                }
                var months = resources.getStringArray(R.array.years)
                for (i in 0 until months.size) {
                    listAge1.add(
                        AgeModel(
                            months[i] + "-" + spnToolBarYear.selectedItem.toString(),
                            false
                        )
                    )
                }
                adapterMonth.notifyDataSetChanged()

            }

        }
    }

    override fun onClick(v: View?) {
        when (v) {
            moreBack -> {
                onBackPressed()
            }
        }
    }


    override fun onBackPressed() {
        finish()
    }
}