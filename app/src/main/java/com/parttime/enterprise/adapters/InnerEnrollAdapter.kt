package com.parttime.enterprise.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.EnrollWorkerProfileActivity
import com.parttime.enterprise.uicomman.fragments.enroll.workesmodels.SingleItemModel


import kotlinx.android.synthetic.main.inroll_worker_inflator.view.*

class InnerEnrollAdapter(context: BaseActivity, list: List<SingleItemModel>) :
    RecyclerView.Adapter<InnerEnrollAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<SingleItemModel>

    var finalList = ArrayList<SingleItemModel>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view =
            LayoutInflater.from(context).inflate(R.layout.inroll_worker_inflator, parent, false)

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
            var rodata = list.get(position)

            // for(i in 0 until rodata.size ){
            view.textView5.text = rodata.title
            view.textView6.text = rodata.subTitle

            if (!rodata.imgUser.toString().isNullOrEmpty())
                Glide.with(context).load(rodata.imgUser).asBitmap().override(100, 100)
                    .placeholder(R.drawable.b_scrren).into(view.imageView_enroll)

            //  Glide.with(context).load(rodata.imgUser).placeholder(R.drawable.b_scrren).into(view.imageView_enroll)

            if (rodata.time.toString().trim().isNotEmpty()) {
                var s = rodata.time.toString().split(" ")
                if (Utilities().isExpire(s[0])) {
                    view.textView7.text = Utilities().getWeekDays(s[0])
                } else {
                    view.textView7.text = Utilities().getCurrentTimeInAmPM(s[1])
                }
            }
            // check work network
            if (rodata.workNetwork == 0) {
                view.imageView3.visibility = View.INVISIBLE
            } else {
                view.imageView3.visibility = View.VISIBLE
            }


            // view click functionality
            view.setOnClickListener {
                var appPrefence = AppPrefence.INSTANCE;
                  appPrefence.initAppPreferences(context);
                var accountType= appPrefence.getInt(AppPrefence.SharedPreferncesKeys.ACCOUNT_TYPE.toString(),0)
                if(accountType==3){
                    val intent = Intent(context, EnrollWorkerProfileActivity::class.java)
                    intent.putExtra("ENROLL_USER_ID", rodata.id)
                    intent.putExtra("TEMP_URL", rodata.imgUser)
                    intent.putExtra("TEMP_TITLE", rodata.title)
                    intent.putExtra("TEMP_SUBTITLE", rodata.subTitle)
                    context.startActivity(intent)
                }else if(accountType==2){
                    val intent = Intent(context, EnrollWorkerProfileActivity::class.java)
                    intent.putExtra("ENROLL_USER_ID", rodata.id)
                    intent.putExtra("TEMP_URL", rodata.imgUser)
                    intent.putExtra("TEMP_TITLE", rodata.title)
                    intent.putExtra("TEMP_SUBTITLE", rodata.subTitle)
                    context.startActivity(intent)
                }else{
                    context.showMessage(context,view,context.resources.getString(R.string.err_account_type))
                }

            }

        }

    }

    /* list item click functionality clik*/
    interface OnItemClickAge {
        fun onClickAge(value: ArrayList<SingleItemModel>)

    }


}