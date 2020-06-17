package com.parttime.enterprise.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantprofile.ApplicantProfileActivity
import com.parttime.enterprise.uicomman.appsearch.searchresults.Message
import com.parttime.enterprise.uicomman.chat.singlechat.SingleChatActivity
import kotlinx.android.synthetic.main.app_search_inflator.view.*

/*Applicant final Search Adapter*/
class AppSearchResultAdapter(context: BaseActivity, list: List<Message>) :
    RecyclerView.Adapter<AppSearchResultAdapter.ViewHolder>() {


    var context: BaseActivity

    private var prevSelection = -1
    var list: List<Message>
    var finalList = ArrayList<Message>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.app_search_inflator, parent, false)

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
            var data = list[position]
            Glide.with(context).load(data.profilePicture).asBitmap().override(100, 100)
                .placeholder(R.drawable.b_scrren).into(view.imageView_appSearch)
            Glide.with(context).load(data.profilePicture).asBitmap().override(100, 100)
                .placeholder(R.drawable.b_scrren).into(view.imageView_appSearch)
            view.txtTitleAppSearch.text = data.firstName
            view.txtSubTitleAppSearch.text = data.lastName
            view.txtLocationAppSearch.text = "--"
            view.txtNationalAppSearch.text = data.nation

            view.imageView_appSearch.setOnClickListener {
                val intent = Intent(context, ApplicantProfileActivity::class.java)
                intent.putExtra("ID", data.userId.toString())
                context.startActivity(intent)
            }
            view.txtTitleAppSearch.setOnClickListener {
                val intent = Intent(context, ApplicantProfileActivity::class.java)
                intent.putExtra("ID", data.userId.toString())
                context.startActivity(intent)
            }
            view.txtSubTitleAppSearch.setOnClickListener {
                val intent = Intent(context, ApplicantProfileActivity::class.java)
                intent.putExtra("ID", data.userId.toString())
                context.startActivity(intent)
            }
            view.txtLocationAppSearch.setOnClickListener {
                val intent = Intent(context, ApplicantProfileActivity::class.java)
                intent.putExtra("ID", data.userId.toString())
                context.startActivity(intent)
            }
            view.txtNationalAppSearch.setOnClickListener {
                val intent = Intent(context, ApplicantProfileActivity::class.java)
                intent.putExtra("ID", data.userId.toString())
                context.startActivity(intent)
            }

            // view data
            view.imgChatAppSearch.setOnClickListener {
                val intent = Intent(context, SingleChatActivity::class.java)
                intent.putExtra("USERID", data.userId)
                intent.putExtra("IMG", data.profilePicture)
                intent.putExtra("NAME", data.firstName)
                context.startActivity(intent)

            }
        }

    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}