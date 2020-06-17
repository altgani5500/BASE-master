package com.parttime.enterprise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.locationaddress.statemodel.Message
import kotlinx.android.synthetic.main.country_list_inflator.view.*


class StateListAdapter(
    private var context: Context,
    private val stateLists: MutableList<Message>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<StateListAdapter.ViewHolder>(),
    Filterable {

    private var prevSelection = -1
    private var msgSearchList: List<Message>? = null
    private val listener1: ItemClickListener? = listener
    var finalList = ArrayList<Message>()

    init {
        this.context = context
        this.msgSearchList = stateLists
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view: View

        init {
            this.view = view
        }

        fun bindData(position: Int) {
            view.rd_countrySelect.setButtonDrawable(R.drawable.sq_checkbox)
            view.rd_countrySelect.text = msgSearchList!![position].state
            view.rd_countrySelect.isChecked = msgSearchList!![position].isClicked
            view.rd_countrySelect.setOnClickListener {
                msgSearchList!![position].isClicked = view.rd_countrySelect.isChecked
                if (finalList.isNotEmpty()) {
                    finalList.clear()
                }
                finalList.addAll(msgSearchList!!)
                listener1?.onItemClicked(finalList)
            }


        }

    }


    override fun onBindViewHolder(holder: StateListAdapter.ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_list_inflator, parent, false)
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
                    msgSearchList = stateLists
                } else {
                    val filteredList = ArrayList<Message>()
                    for (row in stateLists) {
                        if (row.state.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    msgSearchList = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = msgSearchList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                msgSearchList = filterResults.values as ArrayList<Message>
                notifyDataSetChanged()
            }
        }
    }

    interface ItemClickListener {
        fun onItemClicked(states: ArrayList<Message>)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}