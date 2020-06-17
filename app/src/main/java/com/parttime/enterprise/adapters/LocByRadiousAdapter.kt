package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.LocByRadiousModel
import kotlinx.android.synthetic.main.check_box_inflator.view.*

class LocByRadiousAdapter(
    context: BaseActivity,
    list: List<LocByRadiousModel>,
    onCluckListner: OnItemClickLocByRadious
) : RecyclerView.Adapter<LocByRadiousAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<LocByRadiousModel>
    var mOnItemClickListener: OnItemClickLocByRadious? = onCluckListner
    var finalList = ArrayList<LocByRadiousModel>()

    init {
        this.context = context
        this.list = list
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
        var view: View

        init {
            this.view = view
        }

        fun bindData(position: Int) {
            view.inflatorCheckbox.text = list.get(position).name
            view.inflatorCheckbox.isChecked = list.get(position).isClick

            // view click functionality
            view.inflatorCheckbox.setOnClickListener {
                list[position].isClick = view.inflatorCheckbox.isChecked
                if (finalList.isNotEmpty()) {
                    finalList.clear()
                }
                finalList.addAll(list)
                mOnItemClickListener?.onClickLocByRadious(finalList)
            }

        }

    }

    /* list item click functionality clik*/
    interface OnItemClickLocByRadious {
        fun onClickLocByRadious(value: ArrayList<LocByRadiousModel>)

    }


}