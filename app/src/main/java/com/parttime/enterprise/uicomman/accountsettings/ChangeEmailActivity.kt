package com.parttime.enterprise.uicomman.accountsettings

import android.os.Bundle
import android.util.Patterns
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.accountsettings.accountsetting.AccountSettingResponse
import com.parttime.enterprise.viewmodels.ChangeEmailViewModel
import kotlinx.android.synthetic.main.activity_change_email.*
import java.util.regex.Pattern


class ChangeEmailActivity : BaseActivity() {
    val EMAIL_ADDRESS = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    lateinit var viewModel: ChangeEmailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountSettingbck4.rotation = 180F
        } else {
            accountSettingbck4.rotation = 0F
        }
        // back press
        accountSettingbck4.setOnClickListener {
            onBackPressed()
        }
        // on save/update
        AddSave_emial.setOnClickListener {
            if (enter_new_email_name.text.isNotEmpty()) {
                if (isValidEmail(enter_new_email_name.text.toString())) {
                    if (Utilities.isNetworkAvailable(this@ChangeEmailActivity)) {
                        showProgressBarNew()
                        viewModel.changeAccountSettingEnterPrise(
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                            enter_new_email_name.text.toString().trim(),
                            Utilities.isNetworkAvailable(this@ChangeEmailActivity),
                            appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                        )
                    } else {
                        showMessage(this@ChangeEmailActivity, AddSave_emial, getString(R.string.network_error))
                    }
                } else {
                    showMessage(this@ChangeEmailActivity, AddSave_emial, getString(R.string.error_invalid_email))
                }
            } else {
                showMessage(this@ChangeEmailActivity, AddSave_emial, getString(R.string.error_invalid_email))
            }
        }

        getEnterpriseNameResponse()
    }

    private fun getEnterpriseNameResponse() {
        viewModel = ViewModelProviders.of(this@ChangeEmailActivity).get(ChangeEmailViewModel::class.java)
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            showMessage(this@ChangeEmailActivity, AddSave_emial, getString(msg))
        })

        // get Response through observer
        viewModel.accountSettingResponse.observe(this, Observer { response: AccountSettingResponse ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    showMessage(this@ChangeEmailActivity, AddSave_emial, response.message)
                    onBackPressed()
                } else {
                    hideProgressBarNew()
                    showMessage(this@ChangeEmailActivity, AddSave_emial, response.errorMessage.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(this@ChangeEmailActivity, AddSave_emial, response.errorMessage.toString())

            }
        })
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


    override fun onBackPressed() {
        finish()
    }
}