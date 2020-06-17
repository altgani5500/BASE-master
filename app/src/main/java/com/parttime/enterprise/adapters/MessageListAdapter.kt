package com.parttime.enterprise.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.uicomman.chat.singlechat.SingleChatActivity
import com.parttime.enterprise.uicomman.fragments.messages.messagemodel.Message
import de.hdodenhof.circleimageview.CircleImageView

class MessageListAdapter(
    private val context: Context,
    private val msgList: MutableList<Message>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<MessageListAdapter.MessageListAdapter>(),
    Filterable {
    private var msgSearchList: List<Message>? = null

    inner class MessageListAdapter(view: View) : RecyclerView.ViewHolder(view) {
        var name_tv: TextView
        var lastMsg: TextView
        var timeText: TextView
        var countText: TextView
        var picture_iv: CircleImageView

        init {
            name_tv = view.findViewById(R.id.txtMessageUserName)
            lastMsg = view.findViewById(R.id.txtMessageDesc)
            timeText = view.findViewById(R.id.txtMessageUserTime)
            countText = view.findViewById(R.id.txtMessageCount)
            picture_iv = view.findViewById(R.id.message_pic)
            view.setOnClickListener {
                listener.onItemClicked(msgSearchList!![adapterPosition])
            }
        }
    }

    init {
        this.msgSearchList = msgList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListAdapter {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_list_inflator, parent, false)
        return MessageListAdapter(itemView)
    }

    override fun onBindViewHolder(holder: MessageListAdapter, position: Int) {
        val msg = msgSearchList!![position]
        holder.name_tv.text = msg.firstName
        holder.lastMsg.text = msg.lastMessage
        if (!msg.time.isNullOrEmpty()) {
            var s = msg.time.split(" ")
            if (Utilities().isExpire(s[0])) {
                holder.timeText.text = Utilities().getWeekDays(s[0])
            } else {
                holder.timeText.text = Utilities().getCurrentTimeInAmPM(s[1])
            }
        } else {
            holder.timeText.text = "--"
        }

        if (msg.unreadCount == 0) {
            holder.countText.visibility = View.GONE
        } else {
            holder.countText.visibility = View.VISIBLE
            holder.countText.text = msg.unreadCount.toString()
        }

        if (!msg.profilePicture.toString().isNullOrEmpty())
            Glide.with(context).load(msg.profilePicture).asBitmap().override(200, 200)
                .placeholder(R.drawable.b_scrren).into(holder.picture_iv)
        /*Glide.with(context)
            .load(msg.profilePicture)
            .placeholder(R.drawable.b_scrren)
            .into(holder.picture_iv)*/

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SingleChatActivity::class.java)
            intent.putExtra("USERID", msg.receiverId)
            intent.putExtra("IMG", msg.profilePicture)
            intent.putExtra("NAME", msg.firstName + " " + msg.lastName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return msgSearchList!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    msgSearchList = msgList
                } else {
                    val filteredList = ArrayList<Message>()
                    for (row in msgList) {
                        if (row.firstName.toLowerCase().contains(charString.toLowerCase()) || row.lastName.contains(
                                charSequence
                            )
                            || row.lastMessage.contains(charSequence)
                        ) {
                            filteredList.add(row)
                        }
                    }
                    msgSearchList = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = msgSearchList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: Filter.FilterResults
            ) {
                msgSearchList = filterResults.values as ArrayList<Message>
                notifyDataSetChanged()
            }
        }
    }

    interface ItemClickListener {
        fun onItemClicked(dog: Message)
    }
}