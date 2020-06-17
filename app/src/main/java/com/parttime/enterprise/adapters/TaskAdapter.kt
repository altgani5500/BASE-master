package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.TaskModel
import kotlinx.android.synthetic.main.check_box_inflator.view.*

class TaskAdapter(context: BaseActivity, list: List<TaskModel>, onCluckListner: OnItemClickTask) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<TaskModel>
    var mOnItemClickListener: OnItemClickTask? = onCluckListner
    var finalList = ArrayList<TaskModel>()

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
            view.inflatorCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    list.get(position).isClick = true
                    if (finalList.isNotEmpty()) {
                        finalList.clear()
                    }
                    finalList.addAll(list)
                }

                mOnItemClickListener?.onClickTask(finalList)
            }
            /*   view.inflatorCheckbox.setOnClickListener {
                   list[position].isClick = view.inflatorCheckbox.isChecked
                   if (finalList.isNotEmpty()) {
                       finalList.clear()
                   }
                   finalList.addAll(list)
                   mOnItemClickListener?.onClickTask(finalList)
               }*/

        }

    }

    /* list item click functionality clik*/
    interface OnItemClickTask {
        fun onClickTask(value: ArrayList<TaskModel>)

    }


}