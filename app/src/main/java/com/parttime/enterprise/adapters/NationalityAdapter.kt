package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.nationality.Message

import kotlinx.android.synthetic.main.check_box_inflator.view.*

class NationalityAdapter(
    context: BaseActivity,
    list: List<Message>,
    onCluckListner: OnItemClickNationality
) :
    RecyclerView.Adapter<NationalityAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<Message>
    var mOnItemClickListener: OnItemClickNationality? = onCluckListner
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
            view.inflatorCheckbox.text = list.get(position).nationality
            view.inflatorCheckbox.isChecked = list.get(position).isClicked

            // view delete functionality

            view.inflatorCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    list.get(position).isClicked = true
                    if (finalList.isNotEmpty()) {
                        finalList.clear()
                    }
                    finalList.addAll(list)
                }

                mOnItemClickListener?.onClickNationality(finalList)
            }
            /* view.inflatorCheckbox.setOnClickListener {
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
                mOnItemClickListener?.onClickNationality(finalList)
            }*/

        }

    }

    /*Job list item click functionality clik*/
    interface OnItemClickNationality {
        fun onClickNationality(value: ArrayList<Message>)

    }


}