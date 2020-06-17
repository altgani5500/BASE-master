package com.parttime.enterprise.uicomman.accountsettings

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.accountsettings.changepass.ChangePasswordModel
import com.parttime.enterprise.viewmodels.ChangePasswordViewModel
import kotlinx.android.synthetic.main.activity_chnge_pass.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class ChangePasswordActivity : BaseActivity() {

    lateinit var viewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chnge_pass)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountSettingbck3.rotation = 180F
        } else {
            accountSettingbck3.rotation = 0F
        }

        // back button
        accountSettingbck3.setOnClickListener {
            onBackPressed()
        }
        // submit Button
        AddSave_pass.setOnClickListener {
            if (enter_old_pass.text.trim().toString().length > 4) {
                if (isValidPassword(enter_new_password.text.toString().trim())) {
                    if (isValidPassword(enter_conf_password.text.toString())) {
                        if (enter_new_password.text.trim().toString().contentEquals(enter_conf_password.text.trim().toString())) {
                            if (Utilities.isNetworkAvailable(this@ChangePasswordActivity)) {
                                showProgressBarNew()
                                // authKey:String,passOld: String, passNew: String, isConnected: Boolean,lan:String
                                viewModel.changeAccountSettingPassword(
                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString()),
                                    enter_old_pass.text.trim().toString(),
                                    enter_new_password.text.trim().toString(),
                                    Utilities.isNetworkAvailable(this@ChangePasswordActivity),
                                    appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
                                )
                            } else {
                                showMessage(
                                    this@ChangePasswordActivity,
                                    accountSettingbck3,
                                    getString(R.string.network_error)
                                )
                            }
                        }
                    } else {
                        showMessage(
                            this@ChangePasswordActivity,
                            accountSettingbck3,
                            getString(R.string.password_err_new)
                        )
                    }
                } else {
                    showMessage(
                        this@ChangePasswordActivity,
                        accountSettingbck3,
                        getString(R.string.password_err_new)
                    )
                }
            } else {
                showMessage(this@ChangePasswordActivity, accountSettingbck3, getString(R.string.error_invalid_password))
            }
        }

        getPasswordResponse()
    }


    private fun getPasswordResponse() {
        viewModel = ViewModelProviders.of(this@ChangePasswordActivity).get(ChangePasswordViewModel::class.java)
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            showMessage(this@ChangePasswordActivity, accountSettingbck3, getString(msg))
        })

        // get Response through observer
        viewModel.changePasswordModel.observe(this, Observer { response: ChangePasswordModel ->
            hideProgressBarNew()
            if (response.success) {
                if (response.code == 200) {
                    showToast(response.message)
                    onBackPressed()
                } else {
                    hideProgressBarNew()
                    showMessage(this@ChangePasswordActivity, accountSettingbck3, response.errorMessage.toString())
                }
            } else {
                hideProgressBarNew()
                showMessage(this@ChangePasswordActivity, accountSettingbck3, response.errorMessage.toString())

            }
        })
    }


    override fun onBackPressed() {
        finish()
    }


    private fun isValidPassword(password: String): Boolean {
        return if (password.isNotEmpty()) {
            val pattern: Pattern
            val matcher: Matcher
            val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"
            val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{6,20}$"
            pattern = Pattern.compile(PASSWORD_REGEX)
            matcher = pattern.matcher(password)
            matcher.matches()
        } else {
            true
        }
    }
}