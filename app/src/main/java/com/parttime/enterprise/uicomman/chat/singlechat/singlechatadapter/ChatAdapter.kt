package com.parttime.enterprise.uicomman.chat.singlechat.singlechatadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.chat.singlechat.chatlistmodels.ChatMessage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_item_inflator.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatAdapter(context: BaseActivity, list: List<ChatMessage.ChatData>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var context: BaseActivity

    var list: List<ChatMessage.ChatData>
    var finalList = ArrayList<ChatMessage.ChatData>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_item_inflator, parent, false)
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
            if (context.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                    ""
                ).equals("ar")
            ) {
                view.txtReceivedMessage.background =
                    context.resources.getDrawable(R.drawable.bg_chat_received_two)
                view.txtSentMessage.background =
                    context.resources.getDrawable(R.drawable.bg_chat_sent_two)

            } else {
                view.txtReceivedMessage.background =
                    context.resources.getDrawable(R.drawable.bg_chat_received)
                view.txtSentMessage.background =
                    context.resources.getDrawable(R.drawable.bg_chat_sent)
            }
            //  view.inflatorCheckbox.text=list.get(position).name
            if (list[position].sender_type.contentEquals("user")) {
                view.txtReceivedMessage.visibility = View.VISIBLE
                view.txtReceivedTime.visibility = View.VISIBLE
                view.txtSentMessage.visibility = View.GONE
                view.txtSentTime.visibility = View.GONE
                view.txtReceivedMessage.text = list[position].message
                view.txtReceivedTime.text = list[position].created_at
                view.imgReceiver.visibility = View.VISIBLE
                if (list[position].userImage.toString().isNotEmpty() && !list[position].userImage.toString().isNullOrEmpty()) {
                    Picasso.get()
                        .load(list[position].userImage.toString())
                        .placeholder(R.drawable.b_scrren)
                        .into(view.imgReceiver)
                }

                val diffInDays = Utilities.dateDifference(
                    Utilities.stringToDate(
                        list[position].created_at,
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                    ),
                    Utilities.stringToDate(
                        Utilities.getCurrTimeUTC(),
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                    )
                )
                if (diffInDays > 0)
                /* view.txtReceivedTime.text = Utilities.formatDateTime(
                     Utilities.convertUTCtoLocalTime(
                         list[position].created_at
                     ), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd MMM HH:mm"
                 )*/
                    view.txtSentTime.text = parseDateToTimeWithDate(list[position].created_at)
                else
                /*view.txtReceivedTime.text = Utilities.formatDateTime(
                    Utilities.convertUTCtoLocalTime(
                        list[position].created_at
                    ), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "HH:mm"
                )*/
                    view.txtSentTime.text = parseDateToTime(list[position].created_at)
            } else {
                view.txtReceivedMessage.visibility = View.GONE
                view.txtReceivedTime.visibility = View.GONE
                view.txtSentMessage.visibility = View.VISIBLE
                view.txtSentTime.visibility = View.VISIBLE
                view.imgReceiver.visibility = View.GONE
                view.txtSentMessage.text = list[position].message
                view.txtSentTime.text = list[position].created_at

                val diffInDays = Utilities.dateDifference(
                    Utilities.stringToDate(
                        list[position].created_at,
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                    ),
                    Utilities.stringToDate(
                        Utilities.getCurrTimeUTC(),
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                    )
                )
                if (diffInDays > 0)
                    view.txtSentTime.text = parseDateToTimeWithDate(list[position].created_at)/*Utilities.formatDateTime(
                        Utilities.convertUTCtoLocalTime(
                            list[position].created_at
                        ), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "dd MMM HH:mm"
                    )*/
                else
                    view.txtSentTime.text = parseDateToTime(list[position].created_at)/*Utilities.formatDateTime(
                        Utilities.convertUTCtoLocalTime(
                            list[position].created_at
                        ), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "HH:mm"
                    )*/

            }

        }

    }


    // two date for set Time for Chat

    fun parseDateToTime(time: String): String? {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val outputPattern = "hh:mm a"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String? = null

        try {
            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }

    fun parseDateToTimeWithDate(time: String): String? {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val outputPattern = "dd MMM HH:mm"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String? = null

        try {
            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }


}