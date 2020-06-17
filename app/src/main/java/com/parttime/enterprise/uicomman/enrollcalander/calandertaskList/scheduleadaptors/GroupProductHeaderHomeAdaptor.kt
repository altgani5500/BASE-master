package com.cluqe.app.views.activities.home.ui.home.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities.showToastShort
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels.Message
import com.parttime.enterprise.uicomman.enrollcalander.calandertaskList.scheduletasksmodels.User
import kotlinx.android.synthetic.main.calender_task_list_header.view.*


class GroupProductHeaderHomeAdaptor(
    context: BaseActivity,
    list: ArrayList<Message>
) :
    RecyclerView.Adapter<GroupProductHeaderHomeAdaptor.ViewHolder>(),
    GroupProductRowHomeAdaptor.OnItemClickRowItemHomeProduct {

    override fun onClickMenues(value: User) {
        showToastShort(context, value.toString())
    }

    override fun savedMeStatus(value: User) {
        showToastShort(context, value.toString())
    }

    private var prevSelectionLiked = -1
    var context: BaseActivity
    var flagSelection = 1
    var list: MutableList<Message>
    var finalList = ArrayList<Message>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.calender_task_list_header, parent, false)

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
            var headerList = list[position]
            var productList = list[position].userList
            // set advertise data
            view.calanderHeaderDate.text=headerList.time

            // now product list set
            var arrayList = ArrayList<User>()
            arrayList.addAll(productList)
            var rowProductAdaptor =
                GroupProductRowHomeAdaptor(context, arrayList, this@GroupProductHeaderHomeAdaptor)
            view.calanderRecycleViewHeader.setHasFixedSize(true)
            view.calanderRecycleViewHeader.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            view.calanderRecycleViewHeader.adapter = rowProductAdaptor

        }

    }


}