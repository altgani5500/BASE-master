package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.GenderModel
import kotlinx.android.synthetic.main.check_box_inflator.view.*


class GenderAdapter(
    context: BaseActivity,
    list: List<GenderModel>,
    onCluckListner: OnItemClickGender
) :
    RecyclerView.Adapter<GenderAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<GenderModel>
    var mOnItemClickListener: OnItemClickGender? = onCluckListner
    var finalList = ArrayList<GenderModel>()
    private var prevSelection = -1

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
            if (list.get(position).isClick) {
                view.inflatorCheckbox.isChecked = true
                prevSelection = position
            } else {
                view.inflatorCheckbox.isChecked = false
            }
            view.inflatorCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    list[position].isClick = true
                    if (prevSelection >= 0) {
                        list[prevSelection].isClick = false
                        notifyItemChanged(prevSelection)
                    }
                    prevSelection = position
                    mOnItemClickListener?.onClickGender(list[position])
                } else {
                    list[position].isClick = false
                }

            }

        }

    }

    /*Job list item click functionality clik*/
    interface OnItemClickGender {
        fun onClickGender(value: GenderModel)


    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}