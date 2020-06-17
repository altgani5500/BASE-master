package com.parttime.enterprise.uicomman.accountsettings

import android.os.Bundle
import android.view.View
import com.parttime.enterprise.R
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import kotlinx.android.synthetic.main.activity_account_setting.*

class AccountAseetingActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountSettingbck.rotation = 180F
        } else {
            accountSettingbck.rotation = 0F
        }
        ll_edt_firstname.setOnClickListener(this@AccountAseetingActivity)
        ll_edit_last_name.setOnClickListener(this@AccountAseetingActivity)
        ll_changepass.setOnClickListener(this@AccountAseetingActivity)
        ll_change_email.setOnClickListener(this@AccountAseetingActivity)
        accountSettingbck.setOnClickListener(this@AccountAseetingActivity)
    }


    /*Onclick Functionality*/
    override fun onClick(v: View?) {
        when (v) {
            ll_edt_firstname -> {
                launchActivity(ChangeEditFirstNameActivity::class.java)
            }
            ll_edit_last_name -> {
                launchActivity(ChangeLastNameActivity::class.java)
            }
            ll_changepass -> {
                launchActivity(ChangePasswordActivity::class.java)
            }
            ll_change_email -> {
                launchActivity(ChangeEmailActivity::class.java)
            }
            accountSettingbck -> {
                onBackPressed()
            }
        }
    }


    override fun onBackPressed() {
        finish()
    }
}