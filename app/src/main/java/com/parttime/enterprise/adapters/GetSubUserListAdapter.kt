package com.parttime.enterprise.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.parttime.enterprise.R
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.subuser.EditSubUserActivity
import com.parttime.enterprise.uicomman.subuser.adduserlist.Message
import kotlinx.android.synthetic.main.sub_user_list_inflator.view.*


class GetSubUserListAdapter(context: BaseActivity, list: List<Message>) :
    RecyclerView.Adapter<GetSubUserListAdapter.ViewHolder>() {


    var context: BaseActivity


    var list: List<Message>
    var finalList = ArrayList<Message>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view =
            LayoutInflater.from(context).inflate(R.layout.sub_user_list_inflator, parent, false)

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
                view.gtuserD.setBackgroundResource(R.drawable.left_arrow)
            } else {
                view.gtuserD.setBackgroundResource(R.drawable.right_arrow)
            }
            view.addSublist_userName.text = list[position].name
            if (!list[position].subUserLogo.toString().isNullOrEmpty())
                Glide.with(context).load(list[position].subUserLogo.toString()).asBitmap().override(
                    100,
                    100
                )
                    .placeholder(R.drawable.b_scrren).into(view.addSublist_userImg)
            /*  Glide.with(context).load(list[position].subUserLogo).placeholder(R.drawable.b_scrren)
                  .into(view.addSublist_userImg)*/

            // view delete functionality
            view.setOnClickListener {
                val intent = Intent(context, EditSubUserActivity::class.java)
                val gson = Gson()
                val s: String = gson.toJson(list[position])
                intent.putExtra("DATA", s)
                context.startActivity(intent)

            }

        }

    }

    /* list item click functionality clik*/
    interface OnItemClickAge {
        fun onClickAge(value: ArrayList<Message>)
    }


}