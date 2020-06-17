package com.parttime.enterprise.uicomman.accountsettings

import android.os.Bundle
import com.parttime.enterprise.R
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import kotlinx.android.synthetic.main.activity_first_name_change.*

class ChangeEditFirstNameActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_name_change)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountSettingbck1.rotation = 180F
        } else {
            accountSettingbck1.rotation = 0F
        }
    }


}