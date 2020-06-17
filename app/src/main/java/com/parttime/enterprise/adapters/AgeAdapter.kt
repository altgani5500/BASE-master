package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.AgeModel
import kotlinx.android.synthetic.main.check_box_inflator.view.*

/* ***************************************
    This code is for Age Adapter functionality
    this code is not use write now
*
*********************************************/
class AgeAdapter(context: BaseActivity, list: ArrayList<AgeModel>, onCluckListner: OnItemClickAge) :
    RecyclerView.Adapter<AgeAdapter.ViewHolder>() {

    var context: BaseActivity = context

    var list: MutableList<AgeModel> = list
    var mOnItemClickListener: OnItemClickAge? = onCluckListner
    var finalList = ArrayList<AgeModel>()

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
            view.inflatorCheckbox.text = list[position].name
            view.inflatorCheckbox.isChecked = list[position].isClick
            // view click functionality
            view.inflatorCheckbox!!.setOnCheckedChangeListener { buttonView, isChecked ->
                var btn=buttonView
                list.get(position).isClick = isChecked
                if (finalList.isNotEmpty()) {
                    finalList.clear()
                }
                finalList.addAll(list)
                mOnItemClickListener?.onClickAge(finalList)
            }


        }

    }

    /* list item click functionality clik*/
    interface OnItemClickAge {
        fun onClickAge(value: ArrayList<AgeModel>)

    }


}