package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.appsearch.custommodels.SearchFilterModels
import kotlinx.android.synthetic.main.check_box_inflator.view.*

/*Jobs Search page applicant Search Filter adapter*/
class AppSearchAdapter(
    context: BaseActivity,
    list1: List<SearchFilterModels>,
    onCluckListner: OnItemClickAppSearch
) :
    RecyclerView.Adapter<AppSearchAdapter.ViewHolder>() {


    var context: BaseActivity = context

    private var prevSelection = -1
    private var list: List<SearchFilterModels> = list1
    private var mOnItemClickListener: OnItemClickAppSearch? = onCluckListner
    private var finalList = ArrayList<SearchFilterModels>()
    private var eduDeatilPosition = 0

    init {
        for (i in this.list.indices) {
            if(!list.isNullOrEmpty())
            if (list[i].dataType.contentEquals("educationDetail")) {
                eduDeatilPosition = i
                break
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.check_box_inflator, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view: View = view

        fun bindData(position: Int) {
            //gender single click functionality
            view.inflatorCheckbox.buttonDrawable = context.resources.getDrawable(R.drawable.sq_checkbox)
            if (list[position].filterType.contentEquals("gender")) {
                view.searchviewInflator.visibility = View.GONE
                view.inflatorCheckbox.text = list[position].displayName
                view.inflatorCheckbox.isChecked = list[position].isCheckedData
                if (list[position].isCheckedData) {
                    view.inflatorCheckbox.isChecked = true
                    prevSelection = position
                } else {
                    view.inflatorCheckbox.isChecked = false
                }
                view.inflatorCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        list[position].isCheckedData = true
                        if (prevSelection >= 0) {
                            list[prevSelection].isCheckedData = false
                            notifyItemChanged(prevSelection)
                        }
                        prevSelection = position
                        if (finalList.isNotEmpty()) {
                            finalList.clear()
                        }
                        finalList.addAll(list)
                        mOnItemClickListener?.onClickAppSearchGender(finalList)
                    } else {
                        list[position].isCheckedData = false
                    }

                }
            } else {
                prevSelection = -1
                if (list[position].dataType.contentEquals("educationDetaill")) {
                    if (eduDeatilPosition == 1) {
                        view.searchviewInflator.visibility = View.GONE
                        view.inflatorCheckbox.text = list[position].displayName
                        view.inflatorCheckbox.isChecked = list[position].isCheckedData
                    } else {
                        eduDeatilPosition = 1
                        view.searchviewInflator.visibility = View.VISIBLE
                        view.inflatorCheckbox.text = list[position].displayName
                        view.inflatorCheckbox.isChecked = list[position].isCheckedData
                    }
                } else {
                    eduDeatilPosition = 0
                    view.searchviewInflator.visibility = View.GONE
                    view.inflatorCheckbox.text = list[position].displayName
                    view.inflatorCheckbox.isChecked = list[position].isCheckedData
                }

                // view delete functionality
                view.inflatorCheckbox.setOnClickListener {
                    list[position].isCheckedData = view.inflatorCheckbox.isChecked
                    if (finalList.isNotEmpty()) {
                        finalList.clear()
                    }
                    finalList.addAll(list)
                    mOnItemClickListener?.onClickAppSearch(finalList)
                }
            }

        }

    }

    /*Job list item click functionality clik*/
    interface OnItemClickAppSearch {
        fun onClickAppSearch(value: ArrayList<SearchFilterModels>)
        fun onClickAppSearchGender(value: ArrayList<SearchFilterModels>)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}