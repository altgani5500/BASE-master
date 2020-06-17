package com.parttime.com.uicomman.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.helpers.Utilities.isNetworkAvailable
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.auth.authmodels.AuthLoginModel
import com.parttime.enterprise.uicomman.auth.devicetokenmodels.DeviceToken
import com.parttime.enterprise.uicomman.forgots.ForgotActivity
import com.parttime.enterprise.uicomman.homes.HomeActivity
import com.parttime.enterprise.uicomman.homes.homesmodels.HomesModels
import com.parttime.enterprise.viewmodels.AuthViewModel
import kotlinx.android.synthetic.main.acitivity_logins.*


class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var viewModel: AuthViewModel
    lateinit var context: LoginActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeLocale()

        setContentView(R.layout.acitivity_logins)
        // intilise context
        context = this
        apiUserLoginValidate()
        tvForgot.setOnClickListener(this@LoginActivity)
        btnLogin.setOnClickListener(this@LoginActivity)
        // for dummy purpose set user Credential
        /*edtEmail.setText("vikas@techugo.com")
        edtPassword.setText("123456")
*/
        /*   // get device Token
           FirebaseInstanceId.getInstance().instanceId
               .addOnCompleteListener(OnCompleteListener { task ->
                   if (!task.isSuccessful) {
                       Log.w("FCM_MESSAGE", "getInstanceId failed", task.exception)
                       return@OnCompleteListener
                   }
                   // Get new Instance ID token
                   val token = task.result?.token
                   // Log and toast
                   appPrefence.setString(AppPrefence.SharedPreferncesKeys.deviceToken.toString(),token)
                   val msg =  appPrefence.getString(AppPrefence.SharedPreferncesKeys.deviceToken.toString())
                   Log.d("FCM_MESSAGE", msg)
                 //  Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
               })*/

    }

    override fun onClick(v: View?) {
        if (v == tvForgot) {
            launchActivity(ForgotActivity::class.java)
        } else if (v == btnLogin) {
            showProgressBarNew()
            viewModel.loginUserApi(
                edtEmail.text.toString().trim(),
                edtPassword.text.toString().trim(),
                isNetworkAvailable(context),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
            )
        }

    }

    /*  Method to implement viewmodel*/
    private fun apiUserLoginValidate() {
        viewModel = ViewModelProviders.of(context).get(AuthViewModel::class.java)
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(context, edtEmail, getString(msg))
        })
        viewModel.signInResponse.observe(this, Observer { response: AuthLoginModel ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    appPrefence.setString(
                        AppPrefence.SharedPreferncesKeys.name.toString(),
                        response.message.name.toString()
                    )
                    appPrefence.setString(
                        AppPrefence.SharedPreferncesKeys.email.toString(),
                        response.message.email.toString()
                    )
                    appPrefence.setString(
                        AppPrefence.SharedPreferncesKeys.apiToken.toString(),
                        response.message.apiToken.toString()
                    )
                    appPrefence.setString(
                        AppPrefence.SharedPreferncesKeys.enterpriseId.toString(),
                        response.message.enterpriseId.toString()
                    )
                    appPrefence.setString(
                        AppPrefence.SharedPreferncesKeys.socialProvider.toString(),
                        response.message.socialProvider.toString()
                    )
                    if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString(), "").length > 2
                        && appPrefence.getString(AppPrefence.SharedPreferncesKeys.email.toString(), "").length > 5
                    ) {
                        // default Token Set
                        Log.e("DEVKEY", appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()))

                        if (appPrefence.getString(
                                AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                                "1"
                            ).contentEquals("1")
                        ) {
                            if (!appPrefence.getString(AppPrefence.SharedPreferncesKeys.deviceToken.toString()).isNullOrBlank()) {
                                viewModel.deviceTokenApi(
                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.deviceToken.toString()),
                                    "android",
                                    isNetworkAvailable(context),
                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                                )
                                // get Device Token Response
                                getDeviceTokenResponse()
                                appApiSettingCall()
                            } else {
                                // without token update
                                appApiSettingCall()

                                showMessage(context, edtEmail, getString(R.string.sucess_login))
                                launchActivityMain(HomeActivity::class.java)
                            }
                        } else {
                            // Notification is off then
                            appApiSettingCall()
                            showMessage(context, edtEmail, getString(R.string.sucess_login))
                            launchActivityMain(HomeActivity::class.java)
                        }

                        //launchActivityMain(HomeActivity::class.java)
                    }
                } else if (response.code == 204) {
                    hideProgressBarNew()
                    showMessage(context, edtEmail, response.error_message.toString())
                } else if (response.code == 401) {
                    hideProgressBarNew()
                    showMessage(context, edtEmail, response.error_message.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(context, edtEmail, response.error_message.toString())

            }
        })

    }

    private fun appApiSettingCall() {
        if (Utilities.isNetworkAvailable(this@LoginActivity)) {
            // viewModel = ViewModelProviders.of(this@LoginActivity).get(AuthViewModel::class.java)
            viewModel.deviceSetting(
                Utilities.isNetworkAvailable(this@LoginActivity),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
            )
            appSetting()
        } else {
            showToast(getString(R.string.network_error))

        }
    }

    private fun appSetting() {
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            showToast(getString(msg))
        })

        // get Response through observer
        viewModel.settingResponse.observe(this, Observer { response: HomesModels ->
            if (response.success) {
                //   Log.e("HOme Setting ", response.toString())
                if (response.code == 200) {
                    if (response.success) {
                        //  showMessage(this@HomeActivity, navView, response.message.toString())
                        // showToast(response.message.toString())
                        appPrefence.setString(
                            AppPrefence.SharedPreferncesKeys.hourlyRateSetting.toString(),
                            response.hourlyRate
                        )
                        appPrefence.setString(
                            AppPrefence.SharedPreferncesKeys.enrollWorkerSetting.toString(),
                            response.enrolledWorker
                        )
                        if (response.isNotification.toString().contentEquals("1")) {
                            appPrefence.setString(
                                AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                                response.isNotification.toString()
                            )
                        } else {
                            appPrefence.setString(
                                AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                                "0"
                            )
                        }
                        appPrefence.setInt(AppPrefence.SharedPreferncesKeys.ACCOUNT_TYPE.toString(),response.accountType)
                        // app Role  Set into prefence for future
                        /* Schedular  2
                            Supervisor 3
                            Super Evaluator 4
                            Recruiter 5
                            else Enterprise Admin 1
                        * */
                        for (i in 0 until response.enterpriseRole.size) {
                            when (response.enterpriseRole[i]) {

                                "1" -> {
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.ENTERPRISE_ADMIN.toString(),
                                        1
                                    )

                                    // remove
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SCHEDULER.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPERVISIOR.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPER_EVALUATOR.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.RECRUITER.toString(),
                                        0
                                    )

                                }

                                "2" -> {
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SCHEDULER.toString(),
                                        2
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPERVISIOR.toString(),
                                        2
                                    )
                                    //  remove
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPER_EVALUATOR.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.RECRUITER.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.ENTERPRISE_ADMIN.toString(),
                                        0
                                    )


                                }
                                "3" -> {
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPER_EVALUATOR.toString(),
                                        3
                                    )
                                    // remove

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SCHEDULER.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPERVISIOR.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.RECRUITER.toString(),
                                        0
                                    )

                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.ENTERPRISE_ADMIN.toString(),
                                        0
                                    )
                                }

                                "4" -> {
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.RECRUITER.toString(),
                                        4
                                    )
                                    // remove
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPER_EVALUATOR.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SCHEDULER.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.SUPERVISIOR.toString(),
                                        0
                                    )
                                    appPrefence.setInt(
                                        AppPrefence.SharedPreferncesKeys.ENTERPRISE_ADMIN.toString(),
                                        0
                                    )
                                }

                            }


                        }

                    } else {
                        // hideProgressBarNew()
                        showToast(response.errorMessage.toString())
                    }
                } else {
                    showToast(response.errorMessage.toString())
                }
            } else {
                showToast(response.errorMessage.toString())
            }
        })
    }


    private fun getDeviceTokenResponse() {
        viewModel = ViewModelProviders.of(context).get(AuthViewModel::class.java)
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            showMessage(context, edtEmail, getString(msg))
        })

        // get Response through observer
        viewModel.deviceTokenResponse.observe(this, Observer { response: DeviceToken ->
            hideProgressBarNew()
            if (response.success) {
                Log.e("Device Token APi Res", response.toString())
                if (response.code == 200) {
                    //
                    if (response.success) {
                        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString(), "").length > 2
                            && appPrefence.getString(AppPrefence.SharedPreferncesKeys.email.toString(), "").length > 5
                        ) {
                            showMessage(context, edtEmail, getString(R.string.sucess_login))
                            launchActivityMain(HomeActivity::class.java)
                        }
                    } else {
                        hideProgressBarNew()
                        showMessage(context, edtEmail, response.error_message.toString())
                    }
                } else {
                    hideProgressBarNew()
                    showMessage(context, edtEmail, response.error_message.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(context, edtEmail, response.error_message.toString())

            }
        })
    }


}
