package com.parttime.enterprise.adapters


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.Work
import com.parttime.enterprise.uicomman.pinchzoom.PinchZoomActivity
import kotlinx.android.synthetic.main.month_detail_list_inflators.view.*

/*Work Adapter*/
class WeekHistoryListAdapter(
    context: BaseActivity,
    list: List<Work>,
    onCluckListner: OnItemClickWork
) :
    RecyclerView.Adapter<WeekHistoryListAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<Work>
    var mOnItemClickListener: OnItemClickWork? = onCluckListner
    var finalList = ArrayList<Work>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context)
            .inflate(R.layout.month_detail_list_inflators, parent, false)

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
            var data = list.get(position)
            view.txtDate.text = data.date
            view.txtHour.text = data.lateEarlyHour.toString()
            if (data.status.contentEquals(context.getString(R.string.on_time_status))) {
                view.txtHeaderFirst.text = data.status
                view.txtHeaderFirst.setTextColor(context.resources.getColor(R.color.profile_green))
            } else if (data.status.contentEquals(context.getString(R.string.early_hour_status))) {
                view.txtHeaderFirst.text = data.status
                view.txtHeaderFirst.setTextColor(context.resources.getColor(R.color.red))
            } else if (data.status.contentEquals(context.getString(R.string.over_time_status))) {
                view.txtHeaderFirst.text = data.status
                view.txtHeaderFirst.setTextColor(context.resources.getColor(R.color.colorPrimary))
            }else{
                view.txtHeaderFirst.text = data.status
                view.txtHeaderFirst.setTextColor(context.resources.getColor(R.color.colorPrimary))
            }
            view.txtAddSchedule.text = data.schedule.trim()
            view.txtPunchIn.text = data.punchIn
            view.txtPunchOut.text = data.punchOut
            view.txtCancelHour.text = data.workTime.toString()
            if(data.status.contentEquals(context.getString(R.string.on_time_status))){
                view.ll_reason1.visibility=View.GONE
                view.ll_reason2.visibility=View.GONE
                view.ll_regectAndAccept.visibility=View.GONE
            }else if(data.status.contentEquals(context.getString(R.string.early_hour_status)) || data.status.contentEquals(context.getString(R.string.over_time_status)) ){
              if(data.enterpriseStatus.contentEquals(context.getString(R.string.undecided_status))){
                  view.ll_reason2.visibility=View.VISIBLE
                  view.ll_regectAndAccept.visibility=View.VISIBLE
                  view.txtReasionDesc.text = data.reason
                  if (!data.reasonDoc.isNullOrEmpty()) {
                      Glide.with(context).load(data.reasonDoc).asBitmap().override(800, 800)
                          .placeholder(R.drawable.b_scrren).into(view.txtReasonImg)
                  }
              }else{
                  view.ll_reason2.visibility=View.VISIBLE
                  view.ll_regectAndAccept.visibility=View.INVISIBLE
                  view.txtReasionDesc.text = data.reason
                  if (!data.reasonDoc.isNullOrEmpty()) {
                      Glide.with(context).load(data.reasonDoc).asBitmap().override(200, 200)
                          .placeholder(R.drawable.b_scrren).into(view.txtReasonImg)
                  }
              }
                view.ll_reason1.visibility=View.VISIBLE

            }else{
                view.ll_reason1.visibility=View.GONE
                view.ll_reason2.visibility=View.GONE
                view.ll_regectAndAccept.visibility=View.GONE
            }
            view.txtStatus.text = data.enterpriseStatus
            // view click functionality
            view.imgAccepted.setOnClickListener {
                mOnItemClickListener?.onClickWork("Accepted",data.userPunchId)
            }

            view.imgRegected.setOnClickListener {
                mOnItemClickListener?.onClickWork("Rejected",data.userPunchId)
            }
            // image click listner
            view.txtReasonImg.setOnClickListener {
                if(!data.reasonDoc.isNullOrEmpty()) {
                    val ins = Intent(context, PinchZoomActivity::class.java)
                    ins.putExtra("IMGURL", data.reasonDoc)
                    context.startActivity(ins)
                }
            }
        }

    }

    /* list item click functionality clik*/
    interface OnItemClickWork {
        fun onClickWork(value: String,userPunchId:Int)

    }


}