package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.fragments.jobslist.jobslistmodels.Message
import kotlinx.android.synthetic.main.jobs_list_inflator.view.*

class JobsListAdapter(
    context: BaseActivity,
    list: List<Message>,
    onCluckListner: OnItemClickForJobFragment
) :
    RecyclerView.Adapter<JobsListAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<Message>
    var mOnItemClickListener: OnItemClickForJobFragment? = onCluckListner

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.jobs_list_inflator, parent, false)

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
            view.txtJobsTtiles.text = list.get(position).jobTitle
            view.txtJobsCity.text = list.get(position).jobLocation
            view.txtJobsDesc.text = list.get(position).jobDescription
            view.txtJobsApplication.text =
                "" + list.get(position).jobApllicants + " " + context.getString(R.string.no_of_applicnts)
            if (list.get(position).jobStatus.contentEquals("Active") || list.get(position).jobStatus.contentEquals(
                    "نشيط"
                )
            ) {
                view.txtJobsClosed.visibility = View.GONE
            } else {
                view.txtJobsClosed.visibility = View.VISIBLE
            }
            // view delete functionality
            view.jobsDelete.setOnClickListener {
                mOnItemClickListener?.onClickDelete(list[position])
            }
            // view details functionality
            view.ll_view_forDetails.setOnClickListener {
                mOnItemClickListener?.onClickDetails(list[position])
            }
            // view edit jobs functionality
            view.jobsEditJobs.setOnClickListener {
                mOnItemClickListener?.onClickEdit(list[position])
            }


        }

    }

    /*Job list item click functionality clik*/
    interface OnItemClickForJobFragment {
        fun onClickDelete(value: Message)
        fun onClickEdit(value: Message)
        fun onClickDetails(value: Message)

    }


}