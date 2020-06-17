package com.parttime.enterprise.uicomman.enrollworkerprofile.superevaluatorlist

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.parttime.enterprise.R
import com.parttime.enterprise.adapters.SuperEvaluatorListAdapter
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.EditTaskActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.CreateTaskActivity
import com.parttime.enterprise.uicomman.enrollworkerprofile.createtask.superevaluatormodel.Message
import kotlinx.android.synthetic.main.activity_super_evaluator.*


class SuperEvaluatorListActivity : BaseActivity(), SuperEvaluatorListAdapter.ItemClickListener,
    View.OnClickListener {


    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: SuperEvaluatorListAdapter
    lateinit var moreEvaluatorList: ArrayList<Message>
    private var superEvaluaterId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_evaluator)

        val getJsonMore = intent.getStringExtra("MORE")
        val token = object : TypeToken<ArrayList<Message>>() {
        }
        moreEvaluatorList = Gson().fromJson(getJsonMore, token.type)

        inItView()

    }

    override fun onClick(v: View?) {
        when (v) {
            btnEvaluatorSelect -> {
                if (superEvaluaterId != 0) {
                    if (intent.getIntExtra("FLAG", 0) == 1) {
                        val intent =
                            Intent(this@SuperEvaluatorListActivity, CreateTaskActivity::class.java)
                        intent.putExtra("EVEID", superEvaluaterId)
                        setResult(2001, intent)
                        finish()
                    } else if (intent.getIntExtra("FLAG", 0) == 2) {
                        val intent =
                            Intent(this@SuperEvaluatorListActivity, EditTaskActivity::class.java)
                        intent.putExtra("EVEID", superEvaluaterId)
                        setResult(2001, intent)
                        finish()
                    }
                }

            }
            open_SearchbgMsg->{
                ll_inputSearch.visibility = View.VISIBLE
                filterTitle.visibility = View.GONE
                open_SearchbgMsg.visibility = View.GONE
            }
            cancel_SearchbgMsg->{
                if(ll_inputSearch.isVisible) {
                    ll_inputSearch.visibility = View.GONE
                    filterTitle.visibility = View.VISIBLE
                    open_SearchbgMsg.visibility = View.VISIBLE
                    input_messageSearch.setText("")
                }
            }
            accountSettingbck->{
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun inItView() {
        btnEvaluatorSelect.setOnClickListener(this@SuperEvaluatorListActivity)
        open_SearchbgMsg.setOnClickListener(this@SuperEvaluatorListActivity)
        cancel_SearchbgMsg.setOnClickListener(this@SuperEvaluatorListActivity)
        accountSettingbck.setOnClickListener(this@SuperEvaluatorListActivity)

        adapter =
            SuperEvaluatorListAdapter(activity, moreEvaluatorList, this@SuperEvaluatorListActivity)
        superEvaluatorRecycleList.adapter = adapter
        layoutManager = LinearLayoutManager(activity)
        //layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        superEvaluatorRecycleList.layoutManager = layoutManager


        input_messageSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //toast(message = "Number is $s")
                adapter.filter.filter(s)
            }

        })
    }

    override fun onItemClicked(evaluator: Message) {
        //toast(evaluator.name.toString())
        superEvaluaterId = evaluator.evaluatorId
    }
}