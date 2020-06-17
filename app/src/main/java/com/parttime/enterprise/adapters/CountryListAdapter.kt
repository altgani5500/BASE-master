package com.parttime.enterprise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.locationaddress.countrymodel.Message
import kotlinx.android.synthetic.main.country_list_inflator.view.*


class CountryListAdapter(
    private var context: Context,
    private val countryLists: MutableList<Message>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<CountryListAdapter.ViewHolder>(),
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
            view.rd_countrySelect.setButtonDrawable(R.drawable.circle_checkbox)
            view.rd_countrySelect.text = msgSearchList!!.get(position).country
            view.rd_countrySelect.isChecked = msgSearchList!!.get(position).isClicked
            if (msgSearchList!!.get(position).isClicked) {
                view.rd_countrySelect.isChecked = true
                prevSelection = position
            } else {
                view.rd_countrySelect.isChecked = false
            }
            view.rd_countrySelect.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    msgSearchList!![position].isClicked = true
                    if (prevSelection >= 0) {
                        msgSearchList!![prevSelection].isClicked = false
                        notifyItemChanged(prevSelection)
                    }
                    prevSelection = position
                    listener1?.onItemClicked(msgSearchList!![position])
                } else {
                    msgSearchList!![position].isClicked = false
                }

            }

        }

    }


    override fun onBindViewHolder(holder: CountryListAdapter.ViewHolder, position: Int) {
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
                    msgSearchList = countryLists
                } else {
                    val filteredList = ArrayList<Message>()
                    for (row in countryLists) {
                        if (row.country.toLowerCase().contains(charString.toLowerCase())) {
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
        fun onItemClicked(country: Message)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}