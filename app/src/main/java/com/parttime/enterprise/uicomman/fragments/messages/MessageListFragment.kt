package com.parttime.enterprise.uicomman.fragments.messages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.MessageListAdapter
import com.parttime.enterprise.helpers.Utilities.isNetworkAvailable
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseFragment
import com.parttime.enterprise.uicomman.fragments.messages.messagemodel.Message
import com.parttime.enterprise.uicomman.fragments.messages.messagemodel.MessageListResponse
import com.parttime.enterprise.uicomman.notifications.NotificationListActivity
import com.parttime.enterprise.viewmodels.MessageListViewModels
import kotlinx.android.synthetic.main.fagmengt_message_list.*
import kotlinx.android.synthetic.main.fragmengt_applicant.*

class MessageListFragment : BaseFragment(), MessageListAdapter.ItemClickListener {

    private var msgList = ArrayList<com.parttime.enterprise.uicomman.fragments.messages.messagemodel.Message>()
    lateinit var mAdapter: MessageListAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onItemClicked(msg: Message) {
        // item Click Listnetr
    }

    lateinit var viewModel: MessageListViewModels


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fagmengt_message_list, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity).get(MessageListViewModels::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setAdapter()

    }

    private fun setOnClickListener() {
        open_SearchbgMsg.setOnClickListener {
            ll_inputSearch.visibility = View.VISIBLE
            filterTitle.visibility = View.GONE
            open_SearchbgMsg.visibility = View.GONE
        }

        cancel_SearchbgMsg.setOnClickListener {
            if(ll_inputSearch.isVisible){
                ll_inputSearch.visibility = View.GONE
                filterTitle.visibility = View.VISIBLE
                open_SearchbgMsg.visibility = View.VISIBLE
                input_messageSearch.setText("")
            }

        }

        // edit search functionality
        input_messageSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //toast(message = "Number is $s")
                 mAdapter.filter.filter(s)
            }

        })
        // notification list Click Listner
        notify_SearchbgMsg.setOnClickListener {
            activity.launchActivity(NotificationListActivity::class.java)
        }

    }

    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable(context)) {
            activity.showProgressBarNew()
            viewModel.getMessagesApi(
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                isNetworkAvailable(activity)
            )
            apiMSGListResponse()
        } else {
            activity.showMessage(context, recyccleViewMsgList, getString(R.string.network_error))
        }
        checkJobList()
    }

    // msg list get from api
    private fun apiMSGListResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            activity.hideProgressBarNew()
            activity.showMessage(context, recyccleViewMsgList, getString(msg))
        })
        viewModel.msgResponseModel.observe(this, Observer { response: MessageListResponse ->
            activity.hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    // here we need To Call Adapter
                    if (response.success) {
                        if (!msgList.isEmpty())
                            msgList.clear()
                        msgList.addAll(response.message)
                        mAdapter.notifyDataSetChanged()
                        checkJobList()
                    } else {
                        activity.showMessage(context, recyccleViewMsgList, response.errorMessage.toString())
                        checkJobList()
                    }
                } else if (response.code == 204) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, recyccleViewMsgList, response.errorMessage.toString())
                    checkJobList()
                } else if (response.code == 401) {
                    activity.hideProgressBarNew()
                    activity.showMessage(context, recyccleViewMsgList, response.errorMessage.toString())
                    checkJobList()
                }
            } else {
                activity.hideProgressBarNew()
                activity.showMessage(context, recyccleViewMsgList, response.errorMessage.toString())
                checkJobList()
            }
        })

    }

    private fun checkJobList() {
        if (msgList.isNullOrEmpty()) {
            ll_empty_list_home.visibility = View.VISIBLE
            recyccleViewMsgList.visibility = View.GONE
        } else {
            recyccleViewMsgList.visibility = View.VISIBLE
            ll_empty_list_home.visibility = View.GONE
        }
    }

    /*
      Method to set recyclerview adapter
      */
    private fun setAdapter() {
        mAdapter = MessageListAdapter(activity, msgList, this@MessageListFragment)
        recyccleViewMsgList.adapter = mAdapter
        layoutManager = LinearLayoutManager(activity)
        recyccleViewMsgList.layoutManager = layoutManager
    }
}