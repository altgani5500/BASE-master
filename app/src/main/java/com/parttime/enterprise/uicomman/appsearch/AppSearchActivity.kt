package com.parttime.enterprise.uicomman.appsearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.AppSearchAdapter
import com.parttime.enterprise.adapters.AppSearchResultAdapter
import com.parttime.enterprise.helpers.NpaLinearLayoutManager
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.educationdetails.EducationDetails
import com.parttime.enterprise.uicomman.applicantfilter.educationlevel.EducationLevel
import com.parttime.enterprise.uicomman.applicantfilter.nationality.NationalityResponse
import com.parttime.enterprise.uicomman.appsearch.custommodels.SearchFilterModels
import com.parttime.enterprise.uicomman.appsearch.searchresults.SearchResponse
import com.parttime.enterprise.viewmodels.AppSearchViewModel
import kotlinx.android.synthetic.main.activity_app_search.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.timerTask


class AppSearchActivity : BaseActivity(), View.OnClickListener, AppSearchAdapter.OnItemClickAppSearch {


    lateinit var adapterFilter: AppSearchAdapter
    var listModel = ArrayList<SearchFilterModels>()
    lateinit var layoutManager: LinearLayoutManager

    // final search result response
    lateinit var adapterFinalSearch: AppSearchResultAdapter
    var searchResponseList = ArrayList<com.parttime.enterprise.uicomman.appsearch.searchresults.Message>()
    lateinit var layoutManagerSearch: NpaLinearLayoutManager

    // view Model
    lateinit var viewModel: AppSearchViewModel
    var filterWorkingHistrorys = ArrayList<SearchFilterModels>()
    var filterEvolution = ArrayList<SearchFilterModels>()
    var filterAge = ArrayList<SearchFilterModels>()

    var nationalityFilter = ArrayList<SearchFilterModels>()
    var educationLevelFilter = ArrayList<SearchFilterModels>()
    var educationDetaillFilter = ArrayList<SearchFilterModels>()
    var radiusFilter = ArrayList<SearchFilterModels>()
    var taskFilter = ArrayList<SearchFilterModels>()
    var genderFilter = ArrayList<SearchFilterModels>()


    override fun onClickAppSearch(value: ArrayList<SearchFilterModels>) {

        // for remove prev selection elements
        if (!value.isNullOrEmpty()) {
            if (value[0].dataType.contentEquals("workHistory")) {
                if (!filterWorkingHistrorys.isNullOrEmpty())
                    filterWorkingHistrorys.clear()
            }

            if (value[0].dataType.contentEquals("evolution")) {
                if (!filterEvolution.isNullOrEmpty()) {
                    filterEvolution.clear()
                }
            }


            if (value[0].dataType.contentEquals("age")) {
                if (!filterAge.isNullOrEmpty()) {
                    filterAge.clear()
                }

            }
            if (value[0].dataType.contentEquals("nationality")) {
                if (!nationalityFilter.isNullOrEmpty()) {
                    nationalityFilter.clear()
                }

            }
            if (value[0].dataType.contentEquals("educationLevel")) {
                if (!educationLevelFilter.isNullOrEmpty()) {
                    educationLevelFilter.clear()
                }

            }
            if (value[value.size - 1].dataType.contentEquals("educationDetaill")) {
                if (!educationDetaillFilter.isNullOrEmpty()) {
                    educationDetaillFilter.clear()
                }

            }
            if (value[0].dataType.contentEquals("radius")) {
                if (!radiusFilter.isNullOrEmpty()) {
                    radiusFilter.clear()
                }

            }
            if (value[0].dataType.contentEquals("task")) {
                if (!taskFilter.isNullOrEmpty()) {
                    taskFilter.clear()
                }

            }
        }

        // clearFilter()
        // check which is clickable
        for (j in 0 until value.size) {
            /*  if (value[j].isCheckedData) {
                  filterFinalList.add(value[j])
              }*/
            if (value[j].dataType.contentEquals("workHistory")) {
                filterWorkingHistrorys.add(value[j])
            }
            if (value[j].dataType.contentEquals("evolution")) {
                //if(!filterEvolution.isNullOrEmpty()){
                filterEvolution.add(value[j])
                // }
            }
            if (value[j].dataType.contentEquals("age")) {
                //  if(!filterAge.isNullOrEmpty()){
                filterAge.add(value[j])
                //  }
            }
            if (value[j].dataType.contentEquals("nationality")) {
                //  if(!nationalityFilter.isNullOrEmpty()){
                nationalityFilter.add(value[j])
                // }

            }
            if (value[j].dataType.contentEquals("educationLevel")) {
                // if(!educationLevelFilter.isNullOrEmpty()){
                educationLevelFilter.add(value[j])
                // }

            }
            if (value[j].dataType.contentEquals("educationDetaill")) {
                //  if(!educationDetaillFilter.isNullOrEmpty()){
                educationDetaillFilter.add(value[j])
                // }

            }
            if (value[j].dataType.contentEquals("radius")) {
                // if(!radiusFilter.isNullOrEmpty()){
                radiusFilter.add(value[j])
                // }

            }
            if (value[j].dataType.contentEquals("task")) {
                // if(!taskFilter.isNullOrEmpty()){
                taskFilter.add(value[j])
                // }

            }

        }

        // filterFinalList = value
        //showMessage(this@AppSearchActivity, btnSearchGender, value.toString())
    }

    override fun onClickAppSearchGender(value: ArrayList<SearchFilterModels>) {
        if (!value.isNullOrEmpty()) {
            if (value[0].dataType.contentEquals("gender")) {
                if (!genderFilter.isNullOrEmpty())
                    genderFilter.clear()
            }
        }
        for (j in 0 until value.size) {
            if (value[j].dataType.contentEquals("gender")) {
                genderFilter.add(value[j])
            }
        }
        // genderFilter.add(value)
        // showMessage(this@AppSearchActivity, btnSearchGender, value.toString())
    }


    override fun onClick(p0: View?) {
        if(filter_searchView.size>0)
        filter_searchView.stopScroll()
        if(filter_recycleView.size>0)
        filter_recycleView.stopScroll()
        //  val colorBSel = ContextCompat.getColor(activity, R.color.btn_bgtwo)
        val colorSel = ContextCompat.getColor(activity, R.color.white)
        //  val colorBDsel = ContextCompat.getColor(activity, R.color.divider)
        val colorDsel = ContextCompat.getColor(activity, R.color.setting_page_text)
        when (p0) {
            btnSearchGender -> {
                btnSearchGender.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
                btnSearchGender.setTextColor(colorSel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnSearchAge.setTextColor(colorDsel)
                btnJobTitle.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobWorkHistory.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnNationality.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnEducationLevel.setTextColor(colorDsel)
                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocTask.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                btn_AppSearchFilter.visibility = View.VISIBLE
                //check search list is empty or not
                if (!searchResponseList.isNullOrEmpty()) {
                    searchResponseList.clear()
                }
                // gender value set
                if (!listModel.isNullOrEmpty()) {
                    listModel.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                var arr = resources.getStringArray(R.array.gender_array)
                if (!genderFilter.isNullOrEmpty()) {
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                    }

                    for (i in 0 until genderFilter.size) {
                        //filterWorkingHistrory[i].isCheckedData=true
                        //genderFilter.distinctBy { Pair(it.listModel, it.genderFilter) }
                        listModel.add(genderFilter[i])
                    }

                } else {
                    for (i in 0 until arr.size) {
                        listModel.add(
                            SearchFilterModels(
                                "gender", arr[i], "gender", arr[i],
                                false
                            )
                        )
                    }
                }
                adapterFilter.notifyDataSetChanged()
                checkedFilterItem()
            }
            btnSearchAge -> {
                //current btn select

                btnSearchAge.setTextColor(colorSel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
                // other btn de select

                btnSearchGender.setTextColor(colorDsel)
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnJobTitle.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobWorkHistory.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnNationality.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEducationLevel.setTextColor(colorDsel)
                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocTask.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btn_AppSearchFilter.visibility = View.VISIBLE
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                //check search list is empty or not
                if (!searchResponseList.isNullOrEmpty()) {
                    searchResponseList.clear()
                }
                if (!listModel.isNullOrEmpty()) {
                    listModel.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                var arr = resources.getStringArray(R.array.age_array)
                //
                if (!filterAge.isNullOrEmpty()) {
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                    }

                    for (i in 0 until filterAge.size) {
                        //filterWorkingHistrory[i].isCheckedData=true
                        listModel.add(filterAge[i])
                    }
                    //listModel.retainAll(filterWorkingHistrory)
                } else {
                    for (i in 0 until arr.size) {
                        listModel.add(
                            SearchFilterModels(
                                "age", arr[i], "age", arr[i],
                                false
                            )
                        )
                    }
                }
                adapterFilter.notifyDataSetChanged()
                checkedFilterItem()
            }
            btnJobTitle -> {
                //current btn select
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
                btnJobTitle.setTextColor(colorSel)
                // other btn de select
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                btnSearchGender.setTextColor(colorDsel)
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnSearchAge.setTextColor(colorDsel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobWorkHistory.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnNationality.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEducationLevel.setTextColor(colorDsel)
                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocTask.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btn_AppSearchFilter.visibility = View.VISIBLE
            }
            btnJobWorkHistory -> {
                //current btn select
                btnJobWorkHistory.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
                btnJobWorkHistory.setTextColor(colorSel)
                // other btn de select
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnSearchGender.setTextColor(colorDsel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnSearchAge.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobTitle.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnNationality.setTextColor(colorDsel)
                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnEducationLevel.setTextColor(colorDsel)
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnLocTask.setTextColor(colorDsel)
                btn_AppSearchFilter.visibility = View.VISIBLE
                var arr = resources.getStringArray(R.array.working_hour_history)
                //check search list is empty or not
                if (!searchResponseList.isNullOrEmpty()) {
                    searchResponseList.clear()
                }
                if (!listModel.isNullOrEmpty()) {
                    listModel.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                if (!filterWorkingHistrorys.isNullOrEmpty()) {
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                    }
                    for (i in 0 until filterWorkingHistrorys.size) {
                        //filterWorkingHistrory[i].isCheckedData=true
                        listModel.add(filterWorkingHistrorys[i])
                    }
                    //listModel.retainAll(filterWorkingHistrory)
                } else {
                    for (i in 0 until arr.size) {
                        listModel.add(
                            SearchFilterModels(
                                "workHistory", arr[i], "workHistory", arr[i],
                                false
                            )
                        )
                    }

                }
                adapterFilter.notifyDataSetChanged()
                checkedFilterItem()
            }
            btnNationality -> {
                //current btn select
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
                btnNationality.setTextColor(colorSel)
                // other btn de select
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnSearchGender.setTextColor(colorDsel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnSearchAge.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobTitle.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnJobWorkHistory.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnEducationLevel.setTextColor(colorDsel)
                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnLocTask.setTextColor(colorDsel)
                btn_AppSearchFilter.visibility = View.VISIBLE
                //check search list is empty or not
                if (!searchResponseList.isNullOrEmpty()) {
                    searchResponseList.clear()
                }
                if (!listModel.isNullOrEmpty()) {
                    listModel.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                if (Utilities.isNetworkAvailable(this@AppSearchActivity)) {
                    showProgressBarNew()
                    getNationality()
                } else {
                    showMessage(this@AppSearchActivity, btnJobTitle, getString(R.string.network_error))
                }
            }
            btnEducationLevel -> {
                //current btn select
                btnEducationLevel.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
                btnEducationLevel.setTextColor(colorSel)
                // other btn de select
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnSearchGender.setTextColor(colorDsel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnSearchAge.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobTitle.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnJobWorkHistory.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnNationality.setTextColor(colorDsel)
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnLocTask.setTextColor(colorDsel)
                btn_AppSearchFilter.visibility = View.VISIBLE
                //check search list is empty or not
                if (!searchResponseList.isNullOrEmpty()) {
                    searchResponseList.clear()
                }
                if (!listModel.isNullOrEmpty()) {
                    listModel.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                if (Utilities.isNetworkAvailable(this@AppSearchActivity)) {
                    showProgressBarNew()
                    getPaEducationLevel()
                } else {
                    showMessage(this@AppSearchActivity, btnJobTitle, getString(R.string.network_error))
                }

            }
            btnLocByRadious -> {
                //current btn select
                btnLocByRadious.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
                btnLocByRadious.setTextColor(colorSel)
                // other btn de select
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnSearchGender.setTextColor(colorDsel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnSearchAge.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobTitle.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnJobWorkHistory.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnNationality.setTextColor(colorDsel)
                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnEducationLevel.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnLocTask.setTextColor(colorDsel)
                btn_AppSearchFilter.visibility = View.VISIBLE
                //check search list is empty or not
                var arr = resources.getStringArray(R.array.location_by_radious)
                var arr1 = resources.getStringArray(R.array.location_by_radious_selection)
                //check search list is empty or not
                if (!searchResponseList.isNullOrEmpty()) {
                    searchResponseList.clear()
                }
                if (!listModel.isNullOrEmpty()) {
                    listModel.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                if (!radiusFilter.isNullOrEmpty()) {
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                    }
                    for (i in 0 until radiusFilter.size) {
                        //filterWorkingHistrory[i].isCheckedData=true
                        listModel.add(radiusFilter[i])
                    }
                    //listModel.retainAll(filterWorkingHistrory)
                } else {
                    for (i in 0 until arr.size) {
                        listModel.add(
                            SearchFilterModels(
                                "radius", arr1[i], "radius", arr[i],
                                false
                            )
                        )
                    }
                }

                adapterFilter.notifyDataSetChanged()
                checkedFilterItem()
            }
            btnLocTask -> {
                //current btn select
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
                btnLocTask.setTextColor(colorSel)
                // other btn de select
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnSearchGender.setTextColor(colorDsel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnSearchAge.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobTitle.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnJobWorkHistory.setTextColor(colorDsel)
                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnEducationLevel.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnNationality.setTextColor(colorDsel)
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btn_AppSearchFilter.visibility = View.VISIBLE
                //check search list is empty or not
                var arr = resources.getStringArray(R.array.task)
                //  var arr1 = resources.getStringArray(R.array.location_by_radious_selection)
                //check search list is empty or not
                if (!searchResponseList.isNullOrEmpty()) {
                    searchResponseList.clear()
                }
                if (!listModel.isNullOrEmpty()) {
                    listModel.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                if (!taskFilter.isNullOrEmpty()) {
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                    }
                    for (i in 0 until taskFilter.size) {
                        //filterWorkingHistrory[i].isCheckedData=true
                        listModel.add(taskFilter[i])
                    }
                    //listModel.retainAll(filterWorkingHistrory)
                } else {
                    for (i in 0 until arr.size) {
                        listModel.add(
                            SearchFilterModels(
                                "task", arr[i], "task", arr[i],
                                false
                            )
                        )
                    }
                }

                adapterFilter.notifyDataSetChanged()
                checkedFilterItem()
            }
            btnEvolution -> {
                //current btn select
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
                btnEvolution.setTextColor(colorSel)
                // other btn de select
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnSearchGender.setTextColor(colorDsel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnSearchAge.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobTitle.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnJobWorkHistory.setTextColor(colorDsel)
                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnEducationLevel.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnNationality.setTextColor(colorDsel)
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnLocTask.setTextColor(colorDsel)
                btn_AppSearchFilter.visibility = View.VISIBLE
                //check search list is empty or not
                var arr = resources.getStringArray(R.array.evolution)
                //  var arr1 = resources.getStringArray(R.array.location_by_radious_selection)
                //check search list is empty or not
                if (!searchResponseList.isNullOrEmpty()) {
                    searchResponseList.clear()
                }
                if (!listModel.isNullOrEmpty()) {
                    listModel.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                if (!filterEvolution.isNullOrEmpty()) {
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                    }
                    for (i in 0 until filterEvolution.size) {
                        //filterWorkingHistrory[i].isCheckedData=true
                        listModel.add(filterEvolution[i])
                    }
                    //listModel.retainAll(filterWorkingHistrory)
                } else {
                    for (i in 0 until arr.size) {
                        listModel.add(
                            SearchFilterModels(
                                "evolution", arr[i], "evolution", arr[i],
                                false
                            )
                        )
                    }
                }

                adapterFilter.notifyDataSetChanged()
                checkedFilterItem()
            }
            accountSettingbck -> onBackPressed()
            cancel_SearchbgMsg -> {
                // other btn de select
                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnEducationLevel.setTextColor(colorDsel)
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnSearchGender.setTextColor(colorDsel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnSearchAge.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobTitle.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnJobWorkHistory.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnNationality.setTextColor(colorDsel)
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnLocTask.setTextColor(colorDsel)
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                if (!btn_AppSearchFilter.isVisible) {
                    btn_AppSearchFilter.visibility = View.GONE
                }
                // search input blanck set
                input_messageSearch.setText(" ")
                input_messageSearch.hint = resources.getString(R.string.search)
                if (!listModel.isNullOrEmpty()) {
                    listModel.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                clearFilter()
                checkedFilterItem()
            }
            searchReset -> {
                if (!listModel.isNullOrEmpty()) {
                    var newList = ArrayList<SearchFilterModels>()
                    newList.addAll(listModel)
                    listModel.clear()
                    //adapterFilter.notifyDataSetChanged()
                    for (i in 0 until newList.size) {
                        if (newList[i].isCheckedData) {
                            newList[i].isCheckedData = false
                        }
                    }
                    listModel.addAll(newList)
                    newList.clear()
                    adapterFilter.notifyDataSetChanged()
                }
                clearFilter()
                checkedFilterItem()
            }

            btn_AppSearchFilter -> {
                // other btn de select

                btnEducationLevel.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnEducationLevel.setTextColor(colorDsel)
                btnSearchGender.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnSearchGender.setTextColor(colorDsel)
                btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnSearchAge.setTextColor(colorDsel)
                btnJobTitle.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnJobTitle.setTextColor(colorDsel)
                btnJobWorkHistory.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnJobWorkHistory.setTextColor(colorDsel)
                btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnNationality.setTextColor(colorDsel)
                btnLocByRadious.background = ContextCompat.getDrawable(
                    this@AppSearchActivity,
                    R.drawable.btn_d_select
                )
                btnLocByRadious.setTextColor(colorDsel)
                btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnLocTask.setTextColor(colorDsel)
                btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                btnEvolution.setTextColor(colorDsel)
                if (!btn_AppSearchFilter.isVisible) {
                    btn_AppSearchFilter.visibility = View.GONE
                }
                // search input blanck set
                input_messageSearch.setText(" ")
                input_messageSearch.hint = resources.getString(R.string.search)
                // search api hit
                if (Utilities.isNetworkAvailable(this@AppSearchActivity)) {
                    var hashMap = HashMap<String, String>()
                    if (!filterWorkingHistrorys.isNullOrEmpty()) {
                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until filterWorkingHistrorys.size) {
                            if (filterWorkingHistrorys[i].isCheckedData)
                                sb.append(filterWorkingHistrorys[i].idOrSelectedValue).append(",")
                        }
                        value = sb.toString()
                        if (value.trim().length > 0) {
                            value = value.substring(0, value.length - 1)
                            hashMap[filterWorkingHistrorys[0].filterType] = value
                        }

                    }
                    if (!filterEvolution.isNullOrEmpty()) {
                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until filterEvolution.size) {
                            if (filterEvolution[i].isCheckedData)
                                sb.append(filterEvolution[i].idOrSelectedValue).append(",")
                        }
                        value = sb.toString()
                        if (value.trim().length > 0) {
                            value = value.substring(0, value.length - 1)
                            hashMap[filterEvolution[0].filterType] = value
                        }

                    }
                    if (!filterAge.isNullOrEmpty()) {
                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until filterAge.size) {
                            if (filterAge[i].isCheckedData)
                                sb.append(filterAge[i].idOrSelectedValue).append(",")
                        }
                        value = sb.toString()
                        if (value.trim().length > 0) {
                            value = value.substring(0, value.length - 1)
                            hashMap[filterAge[0].filterType] = value
                        }

                    }

                    if (!nationalityFilter.isNullOrEmpty()) {
                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until nationalityFilter.size) {
                            if (nationalityFilter[i].isCheckedData)
                                sb.append(nationalityFilter[i].displayName).append(",")
                        }
                        value = sb.toString()
                        if (value.trim().length > 0) {
                            value = value.substring(0, value.length - 1)
                            hashMap[nationalityFilter[0].filterType] = value
                        }

                    }

                    if (!educationLevelFilter.isNullOrEmpty()) {
                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until educationLevelFilter.size) {
                            if (educationLevelFilter[i].isCheckedData)
                                sb.append(educationLevelFilter[i].displayName).append(",")
                        }
                        value = sb.toString()
                        if (value.trim().length > 0) {
                            value = value.substring(0, value.length - 1)
                            hashMap[educationLevelFilter[0].filterType] = value
                        }

                    }

                    if (!educationDetaillFilter.isNullOrEmpty()) {
                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until educationDetaillFilter.size) {
                            if (educationDetaillFilter[i].isCheckedData)
                                sb.append(educationDetaillFilter[i].displayName).append(",")
                        }
                        value = sb.toString()
                        if (value.trim().length > 0) {
                            value = value.substring(0, value.length - 1)
                            hashMap[educationDetaillFilter[0].filterType] = value
                        }
                    }

                    if (!radiusFilter.isNullOrEmpty()) {
                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until radiusFilter.size) {
                            if (radiusFilter[i].isCheckedData)
                                sb.append(radiusFilter[i].idOrSelectedValue).append(",")
                        }
                        value = sb.toString()
                        if (value.trim().length > 0) {
                            value = value.substring(0, value.length - 1)
                            hashMap[radiusFilter[0].filterType] = value
                        }
                    }

                    if (!taskFilter.isNullOrEmpty()) {
                        var value: String = " "
                        val sb = StringBuilder()
                        for (i in 0 until taskFilter.size) {
                            if (taskFilter[i].isCheckedData)
                                sb.append(taskFilter[i].idOrSelectedValue).append(",")
                        }
                        value = sb.toString()
                        if (value.trim().length > 0) {
                            value = value.substring(0, value.length - 1)
                            hashMap[taskFilter[0].filterType] = value
                        }

                    }
                    if (!genderFilter.isNullOrEmpty()) {
                        for (j in 0 until genderFilter.size) {
                            if (genderFilter[j].isCheckedData)
                                hashMap[genderFilter[0].filterType] = genderFilter[j].idOrSelectedValue
                        }
                    }

                    if (!hashMap.isNullOrEmpty()) {
                        viewModel = ViewModelProviders.of(this@AppSearchActivity).get(AppSearchViewModel::class.java)
                        viewModel.appSearchResponse(
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                            hashMap, Utilities.isNetworkAvailable(this@AppSearchActivity), appPrefence.getString(
                                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()
                            )
                        )
                        checkedFilterItem()
                        getSearchResponse()
                    } else {
                        showMessage(this@AppSearchActivity, input_messageSearch, getString(R.string.filter_message))
                    }
                } else {
                    showMessage(this@AppSearchActivity, input_messageSearch, getString(R.string.network_error))
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_search)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountSettingbck.rotation = 180F
        } else {
            accountSettingbck.rotation = 0F
        }

        inItViewAndClickListner()

        setAdapterInit()
    }

    private fun setAdapterInit() {
        layoutManager = LinearLayoutManager(activity)
        filter_recycleView.layoutManager = layoutManager

        adapterFilter = AppSearchAdapter(activity, listModel, this@AppSearchActivity)
        filter_recycleView.adapter = adapterFilter

        layoutManagerSearch = NpaLinearLayoutManager(activity)
        filter_searchView.layoutManager = layoutManagerSearch
        //Search response Adapter
        adapterFinalSearch = AppSearchResultAdapter(activity, searchResponseList)
        filter_searchView.adapter = adapterFinalSearch

    }

    private fun inItViewAndClickListner() {
        btnSearchGender.setOnClickListener(this@AppSearchActivity)
        btnSearchAge.setOnClickListener(this@AppSearchActivity)
        btnJobTitle.setOnClickListener(this@AppSearchActivity)
        btnJobWorkHistory.setOnClickListener(this@AppSearchActivity)
        btnNationality.setOnClickListener(this@AppSearchActivity)
        btnEducationLevel.setOnClickListener(this@AppSearchActivity)
        accountSettingbck.setOnClickListener(this@AppSearchActivity)
        cancel_SearchbgMsg.setOnClickListener(this@AppSearchActivity)
        btn_AppSearchFilter.setOnClickListener(this@AppSearchActivity)
        btnLocByRadious.setOnClickListener(this@AppSearchActivity)
        btnLocTask.setOnClickListener(this@AppSearchActivity)
        searchReset.setOnClickListener(this@AppSearchActivity)
        btnEvolution.setOnClickListener(this@AppSearchActivity)

        // edit search functionality
        input_messageSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //toast(message = "Number is $s")
                if (!s.isNullOrEmpty()) {
                    // other btn de select
                    // val colorBDsel = ContextCompat.getColor(activity, R.color.divider)
                    val colorDsel = ContextCompat.getColor(activity, R.color.setting_page_text)

                    btnEducationLevel.background = ContextCompat.getDrawable(
                        this@AppSearchActivity,
                        R.drawable.btn_d_select
                    )
                    btnEducationLevel.setTextColor(colorDsel)
                    btnSearchGender.background = ContextCompat.getDrawable(
                        this@AppSearchActivity,
                        R.drawable.btn_d_select
                    )
                    btnSearchGender.setTextColor(colorDsel)
                    btnSearchAge.background = ContextCompat.getDrawable(
                        this@AppSearchActivity,
                        R.drawable.btn_d_select
                    )
                    btnSearchAge.setTextColor(colorDsel)
                    btnJobTitle.background = ContextCompat.getDrawable(
                        this@AppSearchActivity,
                        R.drawable.btn_d_select
                    )
                    btnJobTitle.setTextColor(colorDsel)
                    btnJobWorkHistory.background = ContextCompat.getDrawable(
                        this@AppSearchActivity,
                        R.drawable.btn_d_select
                    )
                    btnJobWorkHistory.setTextColor(colorDsel)
                    btnNationality.background = ContextCompat.getDrawable(
                        this@AppSearchActivity,
                        R.drawable.btn_d_select
                    )
                    btnNationality.setTextColor(colorDsel)
                    btnLocByRadious.background = ContextCompat.getDrawable(
                        this@AppSearchActivity,
                        R.drawable.btn_d_select
                    )
                    btnLocByRadious.setTextColor(colorDsel)
                    btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
                    btnLocTask.setTextColor(colorDsel)
                    btnEvolution.background = ContextCompat.getDrawable(
                        this@AppSearchActivity,
                        R.drawable.btn_d_select
                    )
                    btnEvolution.setTextColor(colorDsel)
                    btn_AppSearchFilter.visibility = View.GONE
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                        adapterFilter.notifyDataSetChanged()
                    }
                    //clearFilter()
                    checkedFilterItem()

                    if (s.trim().length > 2) {
                        if (Utilities.isNetworkAvailable(this@AppSearchActivity)) {
                            var hashMap = HashMap<String, String>()
                            hashMap.put("search", s.toString())
                            viewModel =
                                ViewModelProviders.of(this@AppSearchActivity).get(AppSearchViewModel::class.java)
                            viewModel.appSearchResponse(
                                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                hashMap, Utilities.isNetworkAvailable(this@AppSearchActivity), appPrefence.getString(
                                    AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()
                                )
                            )
                            getSearchResponse()
                        } else {
                            showMessage(
                                this@AppSearchActivity,
                                btnLocTask, getString(R.string.network_error)
                            )
                        }
                    } /*else {
                        showMessage(
                            this@AppSearchActivity,
                            btnLocTask, getString(R.string.search_err_message)
                        )
                    }*/
                }
                // mAdapter!!.filter.filter(s)
            }

        })
    }

    /*App Search Api response*/
    private fun getSearchResponse() {
        // viewModel = ViewModelProviders.of(this@AppSearchActivity).get(AppSearchViewModel::class.java)
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@AppSearchActivity, btn_AppSearchFilter, getString(msg))
        })

        // get Response through observer
        viewModel.searchResultResponse.observe(this, Observer { response: SearchResponse ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                        adapterFilter.notifyDataSetChanged()
                    }
                    if (!searchResponseList.isNullOrEmpty()) {
                         searchResponseList.clear()
                         adapterFinalSearch.notifyDataSetChanged()
                    }
                   // var mList=response.message
                    searchResponseList.addAll(response.message)

                    //filter_searchView.stopScroll()
                  /*  Timer().schedule(timerTask {
                        TODO("Do something")
                    }, 2000)*/
                    filter_searchView.post(Runnable { adapterFinalSearch.notifyDataSetChanged() })
                    checkedSearchresult()
                    checkPrvFilterStatus()
                } else {
                    hideProgressBarNew()
                    showMessage(this@AppSearchActivity, btn_AppSearchFilter, response.errorMessage.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(this@AppSearchActivity, btn_AppSearchFilter, response.errorMessage.toString())

            }
        })
    }

    /*Get education level list From Api*/
    fun getPaEducationLevel() {
        viewModel = ViewModelProviders.of(this@AppSearchActivity).get(AppSearchViewModel::class.java)
        viewModel.getEducationList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@AppSearchActivity, btnJobTitle, getString(msg))
        })

        viewModel.educationLevelResponse.observe(this, androidx.lifecycle.Observer { response: EducationLevel ->
            if (response.success) {
                hideProgressBarNew()
                if (response.code == 200) {
                    // Success Response
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                    }
                    for (i in 0 until response.message.size) {
                        listModel.add(
                            SearchFilterModels(
                                "educationLevel",
                                response.message[i].educationId.toString(),
                                "educationLevel",
                                response.message[i].education,
                                false
                            )
                        )
                    }
                    getEducationDetailList()
                } else if (response.code == 204) {
                    hideProgressBarNew()
                    showMessage(this@AppSearchActivity, btnJobTitle, response.errorMessage.toString())
                } else if (response.code == 401) {
                    hideProgressBarNew()
                    showMessage(this@AppSearchActivity, btnJobTitle, response.errorMessage.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(this@AppSearchActivity, btnJobTitle, response.errorMessage.toString())
            }
        })
    }


    //*Get education details list From Api*//*
    fun getEducationDetailList() {
        viewModel = ViewModelProviders.of(this@AppSearchActivity).get(AppSearchViewModel::class.java)
        viewModel.getEducationDetailList(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            showMessage(this@AppSearchActivity, btnJobTitle, getString(msg))
        })

        viewModel.educationDetailsListResponse.observe(this, androidx.lifecycle.Observer { response: EducationDetails ->
            if (response.success) {
                if (response.code == 200) {
                    // Success Response
                    if (!listModel.isNullOrEmpty()) {
                        if (!listModel[0].filterType.contentEquals("educationLevel")) {
                            listModel.clear()
                        }
                        if (!listModel.isNullOrEmpty() && listModel[listModel.size - 1].filterType.contentEquals("educationDetaill")) {
                            checkedFilterItem()
                        } else {

                            if (!educationLevelFilter.isNullOrEmpty()) {
                                if (!listModel.isNullOrEmpty()) {
                                    listModel.clear()
                                }
                                for (i in 0 until educationLevelFilter.size) {
                                    //filterWorkingHistrory[i].isCheckedData=true
                                    listModel.add(educationLevelFilter[i])
                                }
                                for (i in 0 until educationDetaillFilter.size) {
                                    //filterWorkingHistrory[i].isCheckedData=true
                                    listModel.add(educationDetaillFilter[i])
                                }
                                //listModel.retainAll(filterWorkingHistrory)
                            } else {
                                for (i in 0 until response.message.size) {
                                    listModel.add(
                                        SearchFilterModels(
                                            "educationDetaill",
                                            response.message[i].educationDetailId.toString(),
                                            "educationDetaill",
                                            response.message[i].educationDetail,
                                            false
                                        )
                                    )
                                }
                            }
                            adapterFilter.notifyDataSetChanged()
                            checkedFilterItem()
                        }
                    }

                } else if (response.code == 204) {
                    showMessage(this@AppSearchActivity, btnJobTitle, response.errorMessage.toString())
                } else if (response.code == 401) {
                    showMessage(this@AppSearchActivity, btnJobTitle, response.errorMessage.toString())
                }
            } else {
                showMessage(this@AppSearchActivity, btnJobTitle, response.errorMessage.toString())
            }
        })
    }

    fun getNationality() {
        viewModel = ViewModelProviders.of(this@AppSearchActivity).get(AppSearchViewModel::class.java)
        viewModel.getNationality(appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()))
        viewModel.validationErr.observe(this, androidx.lifecycle.Observer { msg: Int ->
            showMessage(this@AppSearchActivity, btnJobTitle, getString(msg))
            hideProgressBarNew()
        })

        viewModel.nationalLisResponse.observe(this, androidx.lifecycle.Observer { response: NationalityResponse ->
            if (response.success) {
                hideProgressBarNew()
                if (response.code == 200) {
                    // Success Response
                    if (!listModel.isNullOrEmpty()) {
                        listModel.clear()
                    }
                    if (!nationalityFilter.isNullOrEmpty()) {
                        if (!listModel.isNullOrEmpty()) {
                            listModel.clear()
                        }
                        for (i in 0 until nationalityFilter.size) {
                            //filterWorkingHistrory[i].isCheckedData=true
                            listModel.add(nationalityFilter[i])
                        }
                        //listModel.retainAll(filterWorkingHistrory)
                    } else {
                        for (i in 0 until response.message.size) {
                            listModel.add(
                                SearchFilterModels(
                                    "nationality",
                                    response.message[i].nationality.toString(),
                                    "nationality",
                                    response.message[i].nationality,
                                    false
                                )
                            )
                        }
                    }
                    adapterFilter.notifyDataSetChanged()
                    checkedFilterItem()
                } else if (response.code == 204) {
                    hideProgressBarNew()
                    showMessage(this@AppSearchActivity, btnJobTitle, response.errorMessage.toString())
                } else if (response.code == 401) {
                    hideProgressBarNew()
                    showMessage(this@AppSearchActivity, btnJobTitle, response.errorMessage.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(this@AppSearchActivity, btnJobTitle, response.errorMessage.toString())
            }
        })
    }

    private fun checkedFilterItem() {
        if (listModel.isEmpty()) {
            filter_recycleView.visibility = View.GONE
            ll_empty_list_home.visibility = View.VISIBLE
            filter_searchView.visibility = View.GONE
            searchReset.visibility = View.GONE
        } else {
            filter_recycleView.visibility = View.VISIBLE
            searchReset.visibility = View.VISIBLE
            ll_empty_list_home.visibility = View.GONE
            filter_searchView.visibility = View.GONE
        }
    }

    private fun clearFilter() {
        if (!filterWorkingHistrorys.isNullOrEmpty()) {
            filterWorkingHistrorys.clear()
        }
        if (!filterEvolution.isNullOrEmpty()) {
            filterEvolution.clear()
        }
        if (!filterAge.isNullOrEmpty()) {
            filterAge.clear()
        }
        if (!nationalityFilter.isNullOrEmpty()) {
            nationalityFilter.clear()
        }
        if (!educationLevelFilter.isNullOrEmpty()) {
            educationLevelFilter.clear()
        }
        if (!educationDetaillFilter.isNullOrEmpty()) {
            educationDetaillFilter.clear()
        }
        if (!radiusFilter.isNullOrEmpty()) {
            radiusFilter.clear()
        }
        if (!taskFilter.isNullOrEmpty()) {
            taskFilter.clear()
        }
        if (!genderFilter.isNullOrEmpty()) {
            genderFilter.clear()
        }

    }

    private fun checkedSearchresult() {
        if (searchResponseList.isEmpty()) {
            filter_recycleView.visibility = View.GONE
            ll_empty_list_home.visibility = View.VISIBLE
            filter_searchView.visibility = View.GONE
            searchReset.visibility = View.GONE
        } else {
            filter_searchView.visibility = View.VISIBLE
            searchReset.visibility = View.GONE
            filter_recycleView.visibility = View.GONE
            ll_empty_list_home.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun checkPrvFilterStatus() {
        val colorSel = ContextCompat.getColor(activity, R.color.white)
        //  val colorBDsel = ContextCompat.getColor(activity, R.color.divider)
        val colorDsel = ContextCompat.getColor(activity, R.color.setting_page_text)
        if (!filterWorkingHistrorys.isNullOrEmpty()) {
            btnJobWorkHistory.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
            btnJobWorkHistory.setTextColor(colorSel)
        } else {
            btnJobWorkHistory.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
            btnJobWorkHistory.setTextColor(colorDsel)
        }
        if (!genderFilter.isNullOrEmpty()) {
            btnSearchGender.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
            btnSearchGender.setTextColor(colorSel)
        } else {
            btnSearchGender.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
            btnSearchGender.setTextColor(colorDsel)
        }

        if (!filterAge.isNullOrEmpty()) {
            btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
            btnSearchAge.setTextColor(colorSel)
        } else {
            btnSearchAge.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
            btnSearchAge.setTextColor(colorDsel)
        }

        if (!nationalityFilter.isNullOrEmpty()) {
            btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
            btnNationality.setTextColor(colorSel)
        } else {
            btnNationality.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
            btnNationality.setTextColor(colorDsel)
        }

        if (!educationLevelFilter.isNullOrEmpty()) {
            btnEducationLevel.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
            btnEducationLevel.setTextColor(colorSel)
        } else {
            btnEducationLevel.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
            btnEducationLevel.setTextColor(colorDsel)
        }
        if (!radiusFilter.isNullOrEmpty()) {
            btnLocByRadious.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
            btnLocByRadious.setTextColor(colorSel)
        } else {
            btnLocByRadious.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
            btnLocByRadious.setTextColor(colorDsel)
        }
        if (!taskFilter.isNullOrEmpty()) {
            btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
            btnLocTask.setTextColor(colorSel)
        } else {
            btnLocTask.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
            btnLocTask.setTextColor(colorDsel)
        }
        if (!filterEvolution.isNullOrEmpty()) {
            btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_bgtwo)
            btnEvolution.setTextColor(colorSel)
        } else {
            btnEvolution.background = ContextCompat.getDrawable(this@AppSearchActivity, R.drawable.btn_d_select)
            btnEvolution.setTextColor(colorDsel)
        }

    }
}