package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity

import com.parttime.enterprise.uicomman.enrollworkerprofile.monthdetails.monthdetailmodel.Message
import kotlinx.android.synthetic.main.button_inflator.view.*


class TaskHistoryDayAdapter2(
    context: BaseActivity,
    list: List<Message>,
    onCluckListner: OnItemClickDayHistory2
) :
    RecyclerView.Adapter<TaskHistoryDayAdapter2.ViewHolder>() {

    var context: BaseActivity
    var list: List<Message>
    var mOnItemClickListener: OnItemClickDayHistory2? = onCluckListner
    var finalList = ArrayList<Message>()
    private var prevSelection = -1


    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.button_inflator, parent, false)
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
            var item = list[position]
            view.btnInflators.text = item.named
            if (item.isClicked) {
                view.btnInflators.background =
                    ContextCompat.getDrawable(context, R.drawable.btn_bgtwo)
                view.btnInflators.setTextColor(context.resources.getColor(R.color.white))
                prevSelection = position
                // for first time load is enable button this color
                if (finalList.isNotEmpty()) {
                    finalList.clear()
                }
                finalList.addAll(list)
                mOnItemClickListener?.onClickDayHistory(finalList)
            } else {
                view.btnInflators.setTextColor(context.resources.getColor(R.color.setting_page_text))
                view.btnInflators.background =
                    ContextCompat.getDrawable(context, R.drawable.btn_d_select)
            }
            view.btnInflators.tag = position
            view.btnInflators.setOnClickListener {
                var pos = it.tag as Int
                for (i in 0 until list.size) {
                    var items = list[i]
                    items.isClicked = i == pos
                }
                if (finalList.isNotEmpty()) {
                    finalList.clear()
                }
                finalList.addAll(list)
                mOnItemClickListener?.onClickDayHistory(finalList)
                notifyDataSetChanged()

            }

        }

    }

    /*Job list item click functionality clik*/
    interface OnItemClickDayHistory2 {
        fun onClickDayHistory(values: ArrayList<Message>)


    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}