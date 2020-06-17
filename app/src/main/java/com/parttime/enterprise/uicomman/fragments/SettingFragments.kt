package com.parttime.enterprise.uicomman.fragments

import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.com.uicomman.auth.LoginActivity
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseFragment
import com.parttime.enterprise.uicomman.accountsettings.AccountAseetingActivity
import com.parttime.enterprise.uicomman.advancesetting.AdvanceSettingActivity
import com.parttime.enterprise.uicomman.auth.devicetokenmodels.DeviceToken
import com.parttime.enterprise.uicomman.langs.ChangeLanguageActivity
import com.parttime.enterprise.uicomman.notificationSetting.NotificationSettings
import com.parttime.enterprise.uicomman.subuser.AddSubUserActivity
import com.parttime.enterprise.viewmodels.NotificationSettingViewModels
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragments : BaseFragment() {

    lateinit var viewModel: NotificationSettingViewModels

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_setting, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        userRole()
    }

    private fun setOnClickListener() {
        userRole()
        crdLogout.setOnClickListener {
            showDialogBeforDelete(getString(R.string.logout_message))
        }
        setting_lang.setOnClickListener {
            startActivity(Intent(activity, ChangeLanguageActivity::class.java))
        }
        accountSettings.setOnClickListener {
            startActivity(Intent(activity, AccountAseetingActivity::class.java))
        }
        crd_advancekey.setOnClickListener {
            startActivity(Intent(activity, AdvanceSettingActivity::class.java))
        }
        add_some_user.setOnClickListener {
            if (geAccountType() == 1 || geAccountType() == 2 || geAccountType() == 0) {
                activity.showMessage(
                    activity,
                    view,
                    activity.resources.getString(R.string.err_account_type)
                )
            } else if (geAccountType() == 3) {
                startActivity(Intent(activity, AddSubUserActivity::class.java))
            }
        }
        crdviewNotify.setOnClickListener {
            startActivity(Intent(activity, NotificationSettings::class.java))
        }
    }


    /*Custom Dialog For Language Setting*/
    fun showDialogForLanguage(msg: String) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_language)
        val body = dialog.findViewById(R.id.txt_languageDialogTitle) as TextView
        body.text = msg
        val yesBtn = dialog.findViewById(R.id.dialog_lang_apply) as Button
        val noBtn = dialog.findViewById(R.id.dialog_lang_cancel) as Button
        yesBtn.setOnClickListener {

            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    // LOGOUT CUSTOM dIALOG
    /*Custom Dialog For Delete Before*/
    fun showDialogBeforDelete(msg: String) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_for_sure_twobtn)
        val body = dialog.findViewById(R.id.dialog_txtTitle) as TextView
        body.text = msg
        val yesBtn = dialog.findViewById(R.id.dialog_yes) as Button
        val noBtn = dialog.findViewById(R.id.dialog_cancel) as Button
        yesBtn.setOnClickListener {
            var prevApiToken =
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString())
            var enrollSelectedItem =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.enrollWorkerSetting.toString(),
                    "1"
                )
            var hourlySelectedItem =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.hourlyRateSetting.toString(),
                    "1"
                )
            var notifiSetting =
                activity.appPrefence.getString(
                    AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                    "1"
                )
            var lang =
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString())
            var lang_key =
                activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
            activity.appPrefence.clearPreferences()
            activity.appPrefence.setString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(),
                lang
            )
            activity.appPrefence.setString(
                AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(),
                lang_key
            )
            activity.appPrefence.setString(
                AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                notifiSetting
            )
            activity.appPrefence.setString(
                AppPrefence.SharedPreferncesKeys.hourlyRateSetting.toString(),
                hourlySelectedItem
            )
            activity.appPrefence.setString(
                AppPrefence.SharedPreferncesKeys.enrollWorkerSetting.toString(),
                enrollSelectedItem
            )
            val notifManager =
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifManager.cancelAll()
            deviceTokenUpdate(prevApiToken)
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun userRole() {
        if (!enterPriseAdminRole) {
            if (!appSchedulerRole || !appSupervisorRole || !appSupervisorEvaluatorRole) {
                accountSettings.visibility = View.GONE
                crd_advancekey.visibility = View.GONE
                add_some_user.visibility = View.GONE
            } else if (!appRecruiterRole) {
                accountSettings.visibility = View.GONE
                crd_advancekey.visibility = View.GONE
                add_some_user.visibility = View.GONE
            } else {
                accountSettings.visibility = View.VISIBLE
                crd_advancekey.visibility = View.VISIBLE
                add_some_user.visibility = View.VISIBLE
            }
        } else {
            accountSettings.visibility = View.VISIBLE
            crd_advancekey.visibility = View.VISIBLE
            add_some_user.visibility = View.VISIBLE
        }
    }


    private fun deviceTokenUpdate(apiToken: String) {
        // logout Functionality
        viewModel = ViewModelProviders.of(this@SettingFragments)
            .get(NotificationSettingViewModels::class.java)
        viewModel.deviceTokenApi(
            "1234",
            "android",
            Utilities.isNetworkAvailable(activity),
            apiToken,
            activity.appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
        )
        //activity.appPrefence.setString(AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(), "0")
        getDeviceTokenResponse()
    }


    private fun getDeviceTokenResponse() {

        viewModel.validationErr.observe(activity, Observer { msg: Int ->
            activity.showMessage(activity, crdLogout, getString(msg))
        })

        // get Response through observer
        viewModel.deviceTokenResponse.observe(activity, Observer { response: DeviceToken ->
            if (response.success) {
                Log.e("Device Token APi Res", response.toString())
                if (response.code == 200) {
                    if (response.success) {
                        //showMessage(this@NotificationSettings, locationToggle_setting, response.message.toString())
                        activity.launchActivityMain(LoginActivity::class.java)
                    } else {
                        // hideProgressBarNew()
                        activity.showMessage(
                            activity,
                            crdLogout,
                            response.error_message.toString()
                        )
                    }
                } else {
                    activity.showMessage(activity, crdLogout, response.error_message.toString())
                }
            } else {
                activity.showMessage(activity, crdLogout, response.error_message.toString())

            }
        })
    }


}