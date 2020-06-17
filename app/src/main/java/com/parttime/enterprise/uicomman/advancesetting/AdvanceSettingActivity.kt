package com.parttime.enterprise.uicomman.advancesetting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.advancesetting.advancesettingmodel.AdvanceSettingResponse
import com.parttime.enterprise.viewmodels.AdvanceSettingViewModel
import kotlinx.android.synthetic.main.activity_advance_setting.*

class AdvanceSettingActivity : BaseActivity(), View.OnClickListener {

    private var hourlySelectedItem: String = " "
    private var enrollSelectedItem: String = " "
    // view model
    lateinit var viewModel: AdvanceSettingViewModel


    override fun onClick(v: View?) {
        when (v) {
            accountSettingbck_advance -> {
                onBackPressed()
            }
            btnadvanceSetting -> {
                // var hourLySelected=radioGroup.checkedRadioButtonId
                when {
                    rdbhourlyOne.isChecked -> hourlySelectedItem = "1"
                    rdbhourlyTwo.isChecked -> hourlySelectedItem = "2"
                    rdbhourlyThree.isChecked -> hourlySelectedItem = "3"
                }
                when {
                    rdbenrolledOne.isChecked -> enrollSelectedItem = "1"
                    rdbenrolledTwo.isChecked -> enrollSelectedItem = "2"
                }
                if (hourlySelectedItem.trim().isNotEmpty() && enrollSelectedItem.trim().isNotEmpty()) {
                    if (Utilities.isNetworkAvailable(this@AdvanceSettingActivity)) {
                        showProgressBarNew()
                        viewModel.updateAdvanceSetting(
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                            hourlySelectedItem,
                            enrollSelectedItem,
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString()),
                            Utilities.isNetworkAvailable(this@AdvanceSettingActivity)
                        )
                        advanceSettingResponse()
                    } else {
                        showMessage(
                            this@AdvanceSettingActivity,
                            accountSettingbck_advance,
                            getString(R.string.network_error)
                        )
                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advance_setting)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountSettingbck_advance.rotation = 180F
        } else {
            accountSettingbck_advance.rotation = 0F
        }
        initClickListner()
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.hourlyRateSetting.toString(),
                " "
            ).trim().isEmpty()
        ) {
            rdbhourlyOne.isChecked = true
        } else {
            hourlySelectedItem = appPrefence.getString(AppPrefence.SharedPreferncesKeys.hourlyRateSetting.toString())
            when (hourlySelectedItem) {
                "1" -> rdbhourlyOne.isChecked = true
                "2" -> rdbhourlyTwo.isChecked = true
                "3" -> rdbhourlyThree.isChecked = true
            }
        }

        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.enrollWorkerSetting.toString(),
                " "
            ).trim().isEmpty()
        ) {
            rdbenrolledOne.isChecked = true
        } else {
            enrollSelectedItem = appPrefence.getString(AppPrefence.SharedPreferncesKeys.enrollWorkerSetting.toString())
            when (enrollSelectedItem) {
                "1" -> rdbenrolledOne.isChecked = true
                "2" -> rdbenrolledTwo.isChecked = true
            }
        }
    }

    private fun initClickListner() {
        viewModel = ViewModelProviders.of(activity).get(AdvanceSettingViewModel::class.java)
        accountSettingbck_advance.setOnClickListener(this@AdvanceSettingActivity)
        btnadvanceSetting.setOnClickListener(this@AdvanceSettingActivity)
    }

    private fun advanceSettingResponse() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(this@AdvanceSettingActivity, accountSettingbck_advance, getString(msg))
        })
        viewModel.advanceSetingViewModel.observe(this, Observer { response: AdvanceSettingResponse ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    showMessage(this@AdvanceSettingActivity, accountSettingbck_advance, response.message)
                    appPrefence.setString(
                        AppPrefence.SharedPreferncesKeys.hourlyRateSetting.toString(),
                        hourlySelectedItem
                    )
                    appPrefence.setString(
                        AppPrefence.SharedPreferncesKeys.enrollWorkerSetting.toString(),
                        enrollSelectedItem
                    )
                    onBackPressed()
                } else {
                    showMessage(
                        this@AdvanceSettingActivity,
                        accountSettingbck_advance,
                        response.errorMessage.toString()
                    )
                }
            } else {
                showMessage(this@AdvanceSettingActivity, accountSettingbck_advance, response.errorMessage.toString())
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}