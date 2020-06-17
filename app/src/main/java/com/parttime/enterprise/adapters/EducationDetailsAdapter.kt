package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.educationdetails.Message
import kotlinx.android.synthetic.main.check_box_inflator.view.*

class EducationDetailsAdapter(
    context: BaseActivity,
    list: List<Message>,
    onCluckListner: OnItemClickEducationDetails
) :
    RecyclerView.Adapter<EducationDetailsAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<Message>
    var mOnItemClickListener: OnItemClickEducationDetails? = onCluckListner
    var finalList = ArrayList<Message>()

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
            view.inflatorCheckbox.text = list.get(position).educationDetail
            view.inflatorCheckbox.isChecked = list.get(position).isClicked

            // view click functionality
            view.inflatorCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    list.get(position).isClicked = true
                    if (finalList.isNotEmpty()) {
                        finalList.clear()
                    }
                    finalList.addAll(list)
                }

                mOnItemClickListener?.onClickEducationDetails(finalList)
            }
            /*view.inflatorCheckbox.setOnClickListener {
                // list[position].isClick =
                if (view.inflatorCheckbox.isChecked) {
                    view.inflatorCheckbox.isChecked = true
                    list.get(position).isClicked = true
                    finalList.add(list.get(position))
                } else {
                    finalList.remove(list.get(position))
                }

                *//* if(finalList.isNotEmpty()){
                     finalList.clear()
                 }
                 finalList.addAll(list)*//*
                mOnItemClickListener?.onClickEducationDetails(finalList)
            }*/

        }

    }

    /*Job list item click functionality clik*/
    interface OnItemClickEducationDetails {
        fun onClickEducationDetails(value: ArrayList<Message>)

    }


}