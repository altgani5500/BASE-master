package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.AgeModel
import kotlinx.android.synthetic.main.enroll_more_grid_inflator.view.*


/* This code is for Month Adapter functionality */
class MoreMonthAdapter(
    context: BaseActivity,
    list: ArrayList<AgeModel>,
    onCluckListner: OnItemClickMonth
) :
    RecyclerView.Adapter<MoreMonthAdapter.ViewHolder>() {


    var context: BaseActivity

    var list: MutableList<AgeModel>
    var mOnItemClickListener: OnItemClickMonth? = onCluckListner
    var finalList = ArrayList<AgeModel>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view =
            LayoutInflater.from(context).inflate(R.layout.enroll_more_grid_inflator, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view: View

        init {
            this.view = view
        }

        fun bindData(position: Int) {
            var aa = list[position].name.split("-")
            view.txtMoreMonthName.text = aa[0]

            view.txtMoreYear.text = aa[1]
            // view click functionality
            view.setOnClickListener {
                mOnItemClickListener?.onClickMonth(list[position])
            }
        }

    }

    /* list item click functionality clik*/
    interface OnItemClickMonth {
        fun onClickMonth(value: AgeModel)

    }


}