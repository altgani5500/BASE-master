package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.applicantfilter.uimodels.EvaluationModel

import kotlinx.android.synthetic.main.check_box_inflator.view.*

/*Evaluation Adapter*/
class EvaluationAdapter(
    context: BaseActivity,
    list: List<EvaluationModel>,
    onCluckListner: OnItemClickEvaluation
) :
    RecyclerView.Adapter<EvaluationAdapter.ViewHolder>() {


    var context: BaseActivity
    var list: List<EvaluationModel>
    var mOnItemClickListener: OnItemClickEvaluation? = onCluckListner
    var finalList = ArrayList<EvaluationModel>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.check_box_inflator, parent, false)

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
            view.inflatorCheckbox.text = list.get(position).name
            view.inflatorCheckbox.isChecked = list.get(position).isClick

            // view click functionality
            view.inflatorCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    list.get(position).isClick = true
                    if (finalList.isNotEmpty()) {
                        finalList.clear()
                    }
                    finalList.addAll(list)
                }

                mOnItemClickListener?.onClickEvaluation(finalList)
            }
            /* view.inflatorCheckbox.setOnClickListener {
                 list[position].isClick = view.inflatorCheckbox.isChecked
                 if (finalList.isNotEmpty()) {
                     finalList.clear()
                 }
                 finalList.addAll(list)
                 mOnItemClickListener?.onClickEvaluation(finalList)
             }*/

        }

    }

    /* list item click functionality clik*/
    interface OnItemClickEvaluation {
        fun onClickEvaluation(value: ArrayList<EvaluationModel>)

    }


}