package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantprofile.applicantmodel.WorkHistoryList
import kotlinx.android.synthetic.main.applican_worker_history_inflator.view.*

class WorkHistoryListAdapter(context: BaseActivity, list: List<WorkHistoryList>) :
    RecyclerView.Adapter<WorkHistoryListAdapter.ViewHolder>() {


    var context: BaseActivity


    var list: List<WorkHistoryList>
    var finalList = ArrayList<WorkHistoryList>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context)
            .inflate(R.layout.applican_worker_history_inflator, parent, false)

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
            var workHisListItem = list[position]

            //Log.e("FILE_LINK",list[position].docUrl)
            // view click functionality
            if (!workHisListItem.profilePicture.isNullOrEmpty()) {
                Glide.with(context).load(workHisListItem.profilePicture).asBitmap()
                    .override(300, 300)
                    .placeholder(R.drawable.b_scrren).into(view.imgUserPic_dataz)
            }
            if (workHisListItem.isWorking == 1) {
                view.txtWorkHistoryTitle.setTextColor(context.resources.getColor(R.color.colorPrimary))
                view.txtWorkHistoryTitle.text = workHisListItem.jobTitle
            } else {
                view.txtWorkHistoryTitle.setTextColor(context.resources.getColor(R.color.text_bg))
                view.txtWorkHistoryTitle.text = workHisListItem.jobTitle
            }
            view.txtWorkHistoryCompanyName.text = workHisListItem.enterpriseName
            view.txtWorkHistoryTime.text = workHisListItem.schedule.toString()
            view.txtWorkHistoryMonthDetails.text = workHisListItem.jobJoining.toString()
            view.txtWorkHistoryTotalHour.text = workHisListItem.totalWorks.toString()+" "+context.resources.getString(R.string.hours)

        }

    }

    /*Job list item click functionality clik*/
    interface OnItemClickAge {
        fun onClickAge(value: ArrayList<WorkHistoryList>)

    }


}