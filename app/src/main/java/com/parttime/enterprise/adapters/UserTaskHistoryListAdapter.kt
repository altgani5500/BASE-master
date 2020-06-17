package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.UserTaskList
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.WorkHistoryList
import kotlinx.android.synthetic.main.user_task_list_inflator.view.*


class UserTaskHistoryListAdapter(context: BaseActivity, list: List<UserTaskList>) :
    RecyclerView.Adapter<UserTaskHistoryListAdapter.ViewHolder>() {


    var context: BaseActivity


    var list: List<UserTaskList>
    var finalList = ArrayList<UserTaskList>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context)
            .inflate(R.layout.user_task_list_inflator, parent, false)

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
            var userTaskHisListItem = list[position]

            //Log.e("FILE_LINK",list[position].docUrl)
            view.txtUserTaskHistoryEnterPriseName.text=userTaskHisListItem.enterpriseName
            view.txtUserTaskHistoryTaskName.text=userTaskHisListItem.taskName
            if(userTaskHisListItem.taskStatus.equals("Pending")){
                view.txtUserTaskHistoryStstusCircle.background =
                    ContextCompat.getDrawable(context, R.drawable.circle_pending)
                view.txtUserTaskHistoryStstus.setTextColor(context.resources.getColor(R.color.color_pending))
                view.txtUserTaskHistoryStstus.text=context.resources.getString(R.string.pendings)
            }else if(userTaskHisListItem.taskStatus.equals("Complete") || userTaskHisListItem.taskStatus.equals("Completed")){

                if(!userTaskHisListItem.evaluation.isNullOrEmpty()||!userTaskHisListItem.evaluation.isNullOrBlank()){

                    if(userTaskHisListItem.evaluation.toInt()==0){

                        view.txtUserTaskHistoryStstusCircle.background =
                            ContextCompat.getDrawable(context, R.drawable.orange_circle)
                        view.txtUserTaskHistoryStstus.setTextColor(context.resources.getColor(R.color.orange))
                        view.txtUserTaskHistoryStstus.text=context.resources.getString(R.string.pending_evaluation)

                    }else{

                        view.txtUserTaskHistoryStstusCircle.background =
                            ContextCompat.getDrawable(context, R.drawable.circle_overtime)
                        view.txtUserTaskHistoryStstus.setTextColor(context.resources.getColor(R.color.color_overTime))
                        view.txtUserTaskHistoryStstus.text=context.resources.getString(R.string.complete)
                    }
                }
                view.txtUserTaskHistoryStstusCircle.background =
                    ContextCompat.getDrawable(context, R.drawable.circle_overtime)
                view.txtUserTaskHistoryStstus.setTextColor(context.resources.getColor(R.color.color_overTime))
                view.txtUserTaskHistoryStstus.text=context.resources.getString(R.string.complete)
            }
            else if(userTaskHisListItem.taskStatus.equals("In Process") || userTaskHisListItem.taskStatus.equals("InProcess")){
                view.txtUserTaskHistoryStstusCircle.background =
                    ContextCompat.getDrawable(context, R.drawable.circle_green)
                view.txtUserTaskHistoryStstus.setTextColor(context.resources.getColor(R.color.profile_green))
                view.txtUserTaskHistoryStstus.text=context.resources.getString(R.string.in_process)
            }else {
                if(userTaskHisListItem.taskStatus.equals("Rescheduled") || userTaskHisListItem.taskStatus.equals("rescheduled")){
                    view.txtUserTaskHistoryStstusCircle.background =
                        ContextCompat.getDrawable(context, R.drawable.circle_revaluation)
                    view.txtUserTaskHistoryStstus.setTextColor(context.resources.getColor(R.color.color_revolution))
                    view.txtUserTaskHistoryStstus.text=context.resources.getString(R.string.rescheduled_status)
                }else {
                    view.txtUserTaskHistoryStstusCircle.background =
                        ContextCompat.getDrawable(context, R.drawable.circle_revaluation)
                    view.txtUserTaskHistoryStstus.setTextColor(context.resources.getColor(R.color.color_revolution))
                    view.txtUserTaskHistoryStstus.text=context.resources.getString(R.string.revaluation_status)
                }

            }
            if(!userTaskHisListItem.taskType.isNullOrEmpty()) {
                view.rating_tasktypell.visibility=View.VISIBLE
                view.txtUserTaskHistoryTaskType.text =userTaskHisListItem.taskType
            }else{
                view.rating_tasktypell.visibility=View.INVISIBLE
            }
            if(!userTaskHisListItem.evaluatorName.isNullOrEmpty()) {
                view.rating_schedularll.visibility=View.VISIBLE
                view.txtUserTaskHistorySchedular.text =userTaskHisListItem.evaluatorName
            }else{
                view.rating_schedularll.visibility=View.INVISIBLE
            }

            if(!userTaskHisListItem.taskRating.isNullOrEmpty()) {
                view.rating_layoutll.visibility=View.VISIBLE
                view.txtUserTaskHistoryRating.text =userTaskHisListItem.taskRating
            }else{
                view.rating_layoutll.visibility=View.INVISIBLE
            }

        }

    }

    /*Job list item click functionality clik*/
    interface OnItemClickAge {
        fun onClickAge(value: ArrayList<UserTaskList>)

    }


}