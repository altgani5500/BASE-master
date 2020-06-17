package com.parttime.enterprise.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.fragments.enroll.workesmodels.SectionDataModel
import kotlinx.android.synthetic.main.enroll_worker_inner_row_vertical.view.*

class ParrentEnrollAdapter(context: BaseActivity, list: List<SectionDataModel>) :
    RecyclerView.Adapter<ParrentEnrollAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<SectionDataModel>
    lateinit var layoutManager: LinearLayoutManager
    lateinit var workerAdapter: ParrentEnrollAdapter
    var finalList = ArrayList<SectionDataModel>()
    lateinit var innerEnrollAdapter: InnerEnrollAdapter

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context)
            .inflate(R.layout.enroll_worker_inner_row_vertical, parent, false)

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
            view.branchName.text = list.get(position).headerTitle
            //innerEnrollAdapter= InnerEnrollAdapter(context,list.get(position).data)
            //rv_main.adapter=ParrentEnrollAdapter(activity,dummyDataList)
            var singleItemModel = list.get(position).allItemsInSection
            layoutManager = LinearLayoutManager(context)
            view.home_recycler_view_horizontal.layoutManager = layoutManager
            view.home_recycler_view_horizontal.adapter =
                InnerEnrollAdapter(context, singleItemModel)
            view.home_recycler_view_horizontal.layoutManager = layoutManager
/*
            // view delete functionality
            view.inflatorCheckbox.setOnClickListener {
                list[position].isClick = view.inflatorCheckbox.isChecked
                if(finalList.isNotEmpty()){
                    finalList.clear()
                }
                finalList.addAll(list)
                mOnItemClickListener?.onClickAge( finalList)
            }*/

        }

    }

    /* list item click functionality clik*/
    interface OnItemClickAge {
        fun onClickAge(value: ArrayList<SectionDataModel>)

    }


}