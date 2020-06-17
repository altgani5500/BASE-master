package com.parttime.enterprise.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollprofiledetails.EnrollWorkerTaskDetails
import com.parttime.enterprise.uicomman.enrollworkerprofile.enrollworkerprofilelist.Task
import kotlinx.android.synthetic.main.worker_profile_list_inflator.view.*


/* This code is for Evaluator List Adapter functionality */
class WorkerProfileListAdapter(
    context: BaseActivity,
    list: ArrayList<Task>
) :
    RecyclerView.Adapter<WorkerProfileListAdapter.ViewHolder>() {

    var context: BaseActivity

    var list: MutableList<Task>
    var finalList = ArrayList<Task>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context)
            .inflate(R.layout.worker_profile_list_inflator, parent, false)

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
            var task = list[position]
            view.txtTaskListTitleValue.text = task.taskName
            if ("Completed".contentEquals(task.taskStatus.trim())) {

                if (!task.evaluation.isNullOrBlank() || !task.evaluation.isNullOrEmpty()) {

                    if (task.evaluation.toInt() == 0) {
                        view.view_circle.setBackgroundResource(R.drawable.orange_circle)
                        view.txtTaskListStatus.setTextColor(context.resources.getColor(R.color.orange))
                        view.txtTaskListStatus.text =
                            context.resources.getString(R.string.pending_evaluation)
                    } else {
                        view.view_circle.setBackgroundResource(R.drawable.circle_completed)
                        view.txtTaskListStatus.setTextColor(context.resources.getColor(R.color.color_completed))
                        view.txtTaskListStatus.text =
                            context.resources.getString(R.string.completed)
                    }
                }

            } else if ("Pending".contentEquals(task.taskStatus.trim())) {
                view.view_circle.setBackgroundResource(R.drawable.circle_pending)
                view.txtTaskListStatus.setTextColor(context.resources.getColor(R.color.color_pending))
                view.txtTaskListStatus.text = context.resources.getString(R.string.pending)
            } else if ("Over time".contentEquals(task.taskStatus)) {
                view.view_circle.setBackgroundResource(R.drawable.circle_overtime)
                view.txtTaskListStatus.setTextColor(context.resources.getColor(R.color.color_overTime))
                view.txtTaskListStatus.text = context.resources.getString(R.string.over_time_status)
            } else if ("In Process".contentEquals(task.taskStatus)) {
                view.view_circle.setBackgroundResource(R.drawable.circle_green)
                view.txtTaskListStatus.setTextColor(context.resources.getColor(R.color.profile_green))
                view.txtTaskListStatus.text = context.resources.getString(R.string.in_process)
            } else if ("Rescheduled".contentEquals(task.taskStatus)) {
                view.view_circle.setBackgroundResource(R.drawable.circle_revaluation)
                view.txtTaskListStatus.setTextColor(context.resources.getColor(R.color.color_revolution))
                view.txtTaskListStatus.text = context.resources.getString(R.string.rescheduled_status)
            } else if ("ReEvaluation".contentEquals(task.taskStatus)) {
                view.view_circle.setBackgroundResource(R.drawable.circle_revaluation)
                view.txtTaskListStatus.setTextColor(context.resources.getColor(R.color.color_revolution))
                view.txtTaskListStatus.text = context.resources.getString(R.string.revaluation_status)
            }
            if (task.taskType == 1) {
                view.txtTaskListTypeValue.text = context.getString(R.string.continuous)
            } else if (task.taskType == 2) {
                view.txtTaskListTypeValue.text = context.getString(R.string.timebound)
            }

            view.setOnClickListener {
                val intent = Intent(context, EnrollWorkerTaskDetails::class.java)
                intent.putExtra("ID", task.taskId.toString())
                intent.putExtra("USERNAME", task.taskName)
                context.startActivity(intent)

            }
        }


    }


}