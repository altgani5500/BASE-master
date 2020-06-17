package com.parttime.enterprise.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.fragments.applicant.applicantmodel.Message
import kotlinx.android.synthetic.main.inflator_applicant_list.view.*

/* ************************
    Applicant list adapter
    this adaptor class have constructor which is used for set/init
    some use full variables
  ************************/
class ApplicantListAdapter(
    context: BaseActivity,
    list: List<Message>,
    onCluckListner: ApplicantOnItemClickForJobFragment
) : RecyclerView.Adapter<ApplicantListAdapter.ViewHolder>() {
    /*This class scope is class label
    * *******************************/
    private var context: BaseActivity = context
    private  var list: List<Message> = list
    private  var mOnItemClickListener: ApplicantOnItemClickForJobFragment? = onCluckListner

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.inflator_applicant_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view: View = view

        fun bindData(position: Int) {
            if (!list[position].profilePicture.toString().isNullOrEmpty())
                Glide.with(context).load(list[position].profilePicture.toString()).asBitmap().override(
                    100,
                    100
                ).placeholder(R.drawable.b_scrren).into(view.inf_appli_img)
            view.inf_appli_title.text = list[position].name
            view.applicant_workexp.text =  list[position].workExperience.toString() + " " + context.resources.getString(R.string.hours)
            view.applicant_national.text = list[position].nation
            view.applicant_education.text = "" + list[position].education
            view.applicant_appliedon.text = list[position].appliedOn

            Log.e("APPLICATION PROFILE ID", list[position].jobApplyId.toString())
            //view hide show on the basis of status
            if (list[position].status.contentEquals("Accepted") || list[position].status.contentEquals(
                    "وافقت"
                )
            ) {
                view.applicant_Accept.text = list[position].status
                view.inf_appli_acceptance.setBackgroundColor(context.resources.getColor(R.color.applied_color))
                view.inf_appli_acceptance.setText(R.string.accepted_for_interview)
                view.inf_appli_acceptance.isEnabled = false

            } else {
                view.inf_appli_acceptance.setBackgroundColor(context.resources.getColor(R.color.btn_bg))
                view.inf_appli_acceptance.isEnabled = true
                view.ll_applicants.visibility = View.GONE
            }
            // view delete click
            view.inf_appli_delete.setOnClickListener {
                mOnItemClickListener?.onClickDelete(list[position])
            }
            view.inf_appli_chat.setOnClickListener {
                mOnItemClickListener?.onClickChat(list[position])
            }
            // view applicant profile details data click
            view.applicant_ll3.setOnClickListener {
                mOnItemClickListener?.onDetailsClick(list[position])
            }
            // view applicant profile details data click
            view.applicant_ll2.setOnClickListener {
                mOnItemClickListener?.onDetailsClick(list[position])
            }
            // view applicant profile details data clickss
            view.applicant_ll1.setOnClickListener {
                mOnItemClickListener?.onDetailsClick(list[position])
            }

            // final Acceptance
            view.inf_appli_final.setOnClickListener {
                mOnItemClickListener?.onClickFinal(list[position])
            }
            // interview accept
            view.inf_appli_acceptance.setOnClickListener {
                mOnItemClickListener?.onClickApplied(list[position])
            }
        }

    }

    /*Applicant Item Click Functionality*/
    interface ApplicantOnItemClickForJobFragment {
        fun onClickDelete(value: Message)
        fun onClickFinal(value: Message)
        fun onClickChat(value: Message)
        fun onClickApplied(value: Message)
        fun onDetailsClick(value: Message)

    }


}