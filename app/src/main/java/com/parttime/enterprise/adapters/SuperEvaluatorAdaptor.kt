package com.parttime.enterprise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.superevaluatormodel.Message
import kotlinx.android.synthetic.main.super_evaluator_img_inflator.view.*


/* This code is for Evaluator List Adapter functionality */
class SuperEvaluatorAdaptor(
    context: BaseActivity,
    list: ArrayList<Message>,
    onCluckListner: OnItemClickAge
) :
    RecyclerView.Adapter<SuperEvaluatorAdaptor.ViewHolder>() {

    private var prevSelection = -1

    var context: BaseActivity

    var list: MutableList<Message>
    var mOnItemClickListener: OnItemClickAge? = onCluckListner
    var finalList = ArrayList<Message>()

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context)
            .inflate(R.layout.super_evaluator_img_inflator, parent, false)

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
            var superEvaute = list[position]
            if (!superEvaute.profilePicture.isNullOrEmpty()) {
                Glide.with(context).load(superEvaute.profilePicture).asBitmap().override(100, 100)
                    .placeholder(R.drawable.b_scrren).into(view.imgSuperEvaluator)
            }
            if (list.get(position).isClecked) {
                view.imaEvaluatorSelector.visibility = View.VISIBLE
                prevSelection = position
            } else {
                view.imaEvaluatorSelector.visibility = View.GONE
            }
            view.imgSuperEvaluator.setOnClickListener {
                if(!view.imaEvaluatorSelector.isVisible){
                    view.imaEvaluatorSelector.visibility=View.VISIBLE
                    list[position].isClecked = true
                    if (prevSelection >= 0) {
                        list[prevSelection].isClecked = false
                        notifyItemChanged(prevSelection)
                    }
                    prevSelection = position
                    mOnItemClickListener?.onClickAge(list[position])
                }else{
                    view.imaEvaluatorSelector.visibility=View.GONE
                    list[position].isClecked = false
                }

            }
            /*view.imaEvaluatorSelector.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    list[position].isClecked = true
                    if (prevSelection >= 0) {
                        list[prevSelection].isClecked = false
                        notifyItemChanged(prevSelection)
                    }
                    prevSelection = position
                    mOnItemClickListener?.onClickAge(list[position])
                } else {
                    list[position].isClecked = false
                }

            }*/



        }

    }

    /* list item click functionality clik*/
    interface OnItemClickAge {
        fun onClickAge(value: Message)

    }


}