package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.AgeModel

import kotlinx.android.synthetic.main.check_box_inflator.view.*

/*Age Adapter*/
class NewAgeAdapter(context: BaseActivity, list: List<AgeModel>, onCluckListner: OnItemClickAge) :
    RecyclerView.Adapter<NewAgeAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<AgeModel>
    var mOnItemClickListener: OnItemClickAge? = onCluckListner
    var finalList = ArrayList<AgeModel>()

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
            var data = list.get(position)
            view.inflatorCheckbox.text = data.name
            view.inflatorCheckbox.isChecked = data.isClick

            // view click functionality
            view.inflatorCheckbox.setOnClickListener {
                data.isClick = view.inflatorCheckbox.isChecked
                for (i in 0 until list.size) {
                    if (list[i].name.contentEquals(data.name)) {
                        list[i].isClick = data.isClick
                    }
                }
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