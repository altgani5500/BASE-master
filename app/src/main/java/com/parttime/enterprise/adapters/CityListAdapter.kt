package com.parttime.enterprise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.locationaddress.citymodel.MessageX
import kotlinx.android.synthetic.main.country_list_inflator.view.*

/*Location Filter City Adapter*/
class CityListAdapter(
    private var context: Context,
    private val cityList1: MutableList<MessageX>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<CityListAdapter.ViewHolder>(),
    Filterable {

    private var msgSearchList: List<MessageX>? = null
    private val listener1: ItemClickListener? = listener
    private var prevPosition = 1
    var finalList = ArrayList<MessageX>()

    init {
        this.context = context
        this.msgSearchList = cityList1
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view: View

        init {
            this.view = view
        }

        fun bindData(position: Int) {
            var cityList = msgSearchList!![position].cityList
            view.rd_countrySelect.setButtonDrawable(R.drawable.sq_checkbox)
            if (prevPosition == 0) {
                view.city_list_header.text = msgSearchList!![position].stateName
                view.city_list_header.visibility = View.GONE
            } else {
                view.city_list_header.text = msgSearchList!![position].stateName
                view.city_list_header.visibility = View.VISIBLE
            }
            for (i in 0 until cityList.size) {
                view.rd_countrySelect.text = cityList[i].city
                view.rd_countrySelect.isChecked = cityList[i].isClicked
                view.rd_countrySelect.setOnClickListener {
                    cityList[i].isClicked = view.rd_countrySelect.isChecked
                    // cityList!![i].stateNames=msgSearchList!![position].stateName
                    if (finalList.isNotEmpty()) {
                        finalList.clear()
                    }
                    finalList.addAll(msgSearchList!!)
                    listener1?.onItemClicked(finalList)
                }
                if (i == cityList.size - 1) {
                    prevPosition = 1
                } else {
                    prevPosition = 0
                }
            }


        }

    }


    override fun onBindViewHolder(holder: CityListAdapter.ViewHolder, position: Int) {
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
                    msgSearchList = cityList1
                } else {
                    val filteredList = ArrayList<MessageX>()
                    for (row in cityList1) {
                        if (row.stateName.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        } else {
                            for (i in 0 until row.cityList.size) {
                                if (row.cityList[i].city.toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row)
                                }
                            }
                        }
                    }
                    msgSearchList = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = msgSearchList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                msgSearchList = filterResults.values as ArrayList<MessageX>
                notifyDataSetChanged()
            }
        }
    }

    interface ItemClickListener {
        fun onItemClicked(cityList: ArrayList<MessageX>)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}