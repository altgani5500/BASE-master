package com.parttime.enterprise.uicomman.notificationSetting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.auth.devicetokenmodels.DeviceToken
import com.parttime.enterprise.viewmodels.NotificationSettingViewModels
import kotlinx.android.synthetic.main.activity_notification_setting.*

class NotificationSettings : BaseActivity(), View.OnClickListener {

    lateinit var viewModel: NotificationSettingViewModels
    override fun onClick(p0: View?) {
        when (p0) {
            accountSettingbck3 -> onBackPressed()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_setting)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountSettingbck3.rotation = 180F
        } else {
            accountSettingbck3.rotation = 0F
        }
        inItViewANDClickListner()
        // check prev setting of token
        locationToggle_setting.isChecked =
            !appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                " "
            ).contentEquals("0")
        locationToggle_setting.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                viewModel.deviceTokenApi(
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.deviceToken.toString()),
                    "android",
                    Utilities.isNetworkAvailable(this@NotificationSettings),
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                )
                appPrefence.setString(AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(), "1")
            } else {
                viewModel.deviceTokenApi(
                    "1234",
                    "android",
                    Utilities.isNetworkAvailable(this@NotificationSettings),
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                )
                appPrefence.setString(AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(), "0")
            }
            getDeviceTokenResponse()
        }
    }

    private fun inItViewANDClickListner() {
        viewModel = ViewModelProviders.of(this@NotificationSettings).get(NotificationSettingViewModels::class.java)
        accountSettingbck3.setOnClickListener(this@NotificationSettings)
    }


    private fun getDeviceTokenResponse() {

        viewModel.validationErr.observe(this, Observer { msg: Int ->
            showMessage(this@NotificationSettings, locationToggle_setting, getString(msg))
        })

        // get Response through observer
        viewModel.deviceTokenResponse.observe(this, Observer { response: DeviceToken ->
            hideProgressBarNew()
            if (response.success) {
                Log.e("Device Token APi Res", response.toString())
                if (response.code == 200) {
                    if (response.success) {
                        showMessage(this@NotificationSettings, locationToggle_setting, response.message.toString())
                    } else {
                        hideProgressBarNew()
                        showMessage(
                            this@NotificationSettings,
                            locationToggle_setting,
                            response.error_message.toString()
                        )
                    }
                } else {
                    hideProgressBarNew()
                    showMessage(this@NotificationSettings, locationToggle_setting, response.error_message.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(this@NotificationSettings, locationToggle_setting, response.error_message.toString())

            }
        })
    }

    override fun onBackPressed() {
        finish()
    }
}