package com.parttime.enterprise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.superevaluatormodel.Message
import kotlinx.android.synthetic.main.super_evaluator_list_inflator.view.*


class SuperEvaluatorListAdapter(
    private var context: Context,
    private val countryLists: MutableList<Message>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<SuperEvaluatorListAdapter.ViewHolder>(),
    Filterable {

    private var prevSelection = -1
    private var msgSearchList: List<Message>? = null
    private val listener1: ItemClickListener? = listener

    init {
        this.context = context
        this.msgSearchList = countryLists
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view: View

        init {
            this.view = view
        }

        fun bindData(position: Int) {
            if (!msgSearchList?.get(position)?.profilePicture.isNullOrEmpty()) {
                Glide.with(context).load(msgSearchList?.get(position)?.profilePicture).asBitmap()
                    .override(100, 100)
                    .placeholder(R.drawable.b_scrren).into(view.imageView_enroll)
            }
            view.textView5.text = msgSearchList?.get(position)?.name
            view.textView7.setButtonDrawable(R.drawable.circle_checkbox)
            view.textView7.isChecked = msgSearchList!!.get(position).isClecked
            if (msgSearchList!!.get(position).isClecked) {
                view.textView7.isChecked = true
                prevSelection = position
            } else {
                view.textView7.isChecked = false
            }
            view.textView7.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    msgSearchList!![position].isClecked = true
                    if (prevSelection >= 0) {
                        msgSearchList!![prevSelection].isClecked = false
                        notifyItemChanged(prevSelection)
                    }
                    prevSelection = position
                    listener1?.onItemClicked(msgSearchList!![position])
                } else {
                    msgSearchList!![position].isClecked = false
                }

            }

        }

    }


    override fun onBindViewHolder(holder: SuperEvaluatorListAdapter.ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.super_evaluator_list_inflator, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return msgSearchList!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    msgSearchList = countryLists
                } else {
                    val filteredList = ArrayList<Message>()
                    for (row in countryLists) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    msgSearchList = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = msgSearchList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: Filter.FilterResults
            ) {
                msgSearchList = filterResults.values as ArrayList<Message>
                notifyDataSetChanged()
            }
        }
    }

    interface ItemClickListener {
        fun onItemClicked(evaluator: Message)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}