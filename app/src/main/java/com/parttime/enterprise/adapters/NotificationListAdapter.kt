package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.notifications.notificationmodels.Message
import kotlinx.android.synthetic.main.notification_list_inflator.view.*


class NotificationListAdapter(context: BaseActivity, list: List<Message>) :
    RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<Message>

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view =
            LayoutInflater.from(context).inflate(R.layout.notification_list_inflator, parent, false)

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
            var msg = list[position]
            Glide.with(context).load(msg.profilePicture).placeholder(R.drawable.b_scrren)
                .into(view.notfyUserImg)
            view.notificationTitle.text = msg.firstName
            view.notificationSubTitle.text = msg.message
            //view.notificationTime.setText(msg.time)
            // convert date Standerd Formet
            if (!msg.time.isNullOrEmpty()) {
                var serverDate = msg.time
                var newDate = Utilities.formatDateTime(
                    Utilities.convertUTCtoLocalTime2(
                        serverDate
                    ), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                )
                msg.time = newDate
                if (!msg.time.isNullOrEmpty()) {
                    val diffInDays = Utilities.dateDifference(
                        Utilities.stringToDate(
                            msg.time,
                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                        ),
                        Utilities.stringToDate(
                            Utilities.getCurrTimeUTC(),
                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                        )
                    )
                    if (diffInDays > 0)
                        view.notificationTime.text = Utilities.formatDateTime(
                            Utilities.convertUTCtoLocalTime(
                                msg.time
                            ), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd MMM "
                        )
                    else
                        view.notificationTime.text = Utilities.formatDateTime(
                            Utilities.convertUTCtoLocalTime(
                                msg.time
                            ), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "HH:mm"
                        )
                }
            }
        }

    }


}