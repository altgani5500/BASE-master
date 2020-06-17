package com.cluqe.app.views.activities.home.ui.home.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.editschedule.EditSchedulesActivity
import com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels.User
import kotlinx.android.synthetic.main.calander_task_list_inflator.view.*



class GroupProductRowHomeAdaptor(
    context: BaseActivity,
    list: ArrayList<User>,
    onCluckListner: OnItemClickRowItemHomeProduct
) :
    RecyclerView.Adapter<GroupProductRowHomeAdaptor.ViewHolder>() {

    private var prevSelectionLiked = -1
    var mOnItemClickListener: OnItemClickRowItemHomeProduct? = onCluckListner
    var context: BaseActivity
    var flagSelection = 1
    var list: MutableList<User>

    var finalList = ArrayList<User>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.calander_task_list_inflator, parent, false)

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
            var highlight = list[position]

            if (highlight.prifilePicture.isNullOrBlank()) {
                Glide.with(context).load(R.drawable.others).into(view.imgCalander)
            } else {
                Glide.with(context).load(highlight.prifilePicture).into(view.imgCalander)
            }
            // price set
            view.taskTextUserName.text = highlight.name
            view.taskTextUserTitle.text=highlight.jobTitle

            view.imgCalanderEdit.setOnClickListener {
                var intents=Intent(context,EditSchedulesActivity::class.java)
                intents.putExtra("SCHEDULE_TIMING",highlight.scheduleTimingId.toString())
                intents.putExtra("NAME",highlight.name)
                intents.putExtra("PROFILE_IMG",highlight.prifilePicture)
                intents.putExtra("JOB_TITLE",highlight.jobTitle)
                intents.putExtra("START_TIME",highlight.startTime)
                intents.putExtra("END_TIME",highlight.endTime)
                intents.putExtra("SCHEDULE_DATE",highlight.scheduleDate)
                context.startActivityForResult(intents,1003)

            }
        }
    }

    /* list item click functionality clik*/
    interface OnItemClickRowItemHomeProduct {
        fun onClickMenues(value: User)
        fun savedMeStatus(value:User)

    }

}


