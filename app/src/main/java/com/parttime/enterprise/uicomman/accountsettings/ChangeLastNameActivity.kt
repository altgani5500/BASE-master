package com.parttime.enterprise.uicomman.accountsettings

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.accountsettings.accountsetting.AccountSettingResponse
import com.parttime.enterprise.viewmodels.ChangeLastNameViewModel
import kotlinx.android.synthetic.main.activity_edit_last_name.*

class ChangeLastNameActivity : BaseActivity() {

    lateinit var viewModel: ChangeLastNameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_last_name)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountSettingbck2.rotation = 180F
        } else {
            accountSettingbck2.rotation = 0F
        }

        // Api hit and get Response
        AddSave_enterprise.setOnClickListener {
            if (enter_new_last_name.text.toString().trim().length > 3) {
                if (Utilities.isNetworkAvailable(this@ChangeLastNameActivity)) {
                    showProgressBarNew()
                    viewModel.changeAccountSettingEnterPrise(
                        appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                        enter_new_last_name.text.toString().trim(),
                        Utilities.isNetworkAvailable(this@ChangeLastNameActivity),
                        appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                    )
                } else {
                    showMessage(this@ChangeLastNameActivity, accountSettingbck2, getString(R.string.network_error))
                }
            } else {
                showMessage(this@ChangeLastNameActivity, accountSettingbck2, getString(R.string.enter_name_please))
            }
        }

        accountSettingbck2.setOnClickListener {
            onBackPressed()
        }

        getEnterpriseNameResponse()


    }


    private fun getEnterpriseNameResponse() {
        viewModel = ViewModelProviders.of(this@ChangeLastNameActivity).get(ChangeLastNameViewModel::class.java)
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            showMessage(this@ChangeLastNameActivity, accountSettingbck2, getString(msg))
        })

        // get Response through observer
        viewModel.accountSettingResponse.observe(this, Observer { response: AccountSettingResponse ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    showMessage(this@ChangeLastNameActivity, accountSettingbck2, response.message)
                    onBackPressed()
                } else {
                    hideProgressBarNew()
                    showMessage(this@ChangeLastNameActivity, accountSettingbck2, response.errorMessage.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(this@ChangeLastNameActivity, accountSettingbck2, response.errorMessage.toString())

            }
        })
    }


    override fun onBackPressed() {
        finish()
    }
}