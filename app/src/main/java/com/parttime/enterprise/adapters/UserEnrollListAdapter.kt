package com.parttime.enterprise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.schedules.allenrolllist.Message
import kotlinx.android.synthetic.main.schedule_enroll_user_inflator.view.*


/*Get All User Enroll Adapter*/
class UserEnrollListAdapter(
    private var context: Context,
    private val cityList1: MutableList<Message>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<UserEnrollListAdapter.ViewHolder>() {

    private var msgSearchList: List<Message>? = null
    private val listener1: ItemClickListener? = listener
    private var prevPosition = 1
    var finalList = ArrayList<Message>()

    init {
        this.context = context
        this.msgSearchList = cityList1
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view: View

        init {
            this.view = view
        }

        fun bindData(position: Int) {
            var enrollUser = msgSearchList!![position]
            if (!enrollUser.profilePicture.isNullOrEmpty()) {
                Glide.with(context).load(enrollUser.profilePicture).asBitmap().override(100, 100)
                    .placeholder(R.drawable.b_scrren).into(view.imgUserPic_data)
            }
            view.txtProfileName.text = enrollUser.name
            view.txtProfileNationality.text = enrollUser.jobTitle
            view.continousCheckBox.isChecked = msgSearchList!![position].isClicked
            // view click functionality
            view.continousCheckBox!!.setOnCheckedChangeListener { buttonView, isChecked ->
                msgSearchList!![position].isClicked = isChecked
                if (finalList.isNotEmpty()) {
                    finalList.clear()
                }
                finalList.addAll(msgSearchList!!)
                listener1?.onItemClicked(finalList)
            }


        }

    }


    override fun onBindViewHolder(holder: UserEnrollListAdapter.ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.schedule_enroll_user_inflator, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return msgSearchList!!.size
    }


    interface ItemClickListener {
        fun onItemClicked(cityList: ArrayList<Message>)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}