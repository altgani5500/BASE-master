package com.parttime.enterprise.uicomman.forgots

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.parttime.enterprise.apiclients.ServerConstant
import com.parttime.com.uicomman.auth.LoginActivity
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.forgots.forgotModel.ForgotModelResponse
import com.parttime.enterprise.viewmodels.ForgotViewModel
import kotlinx.android.synthetic.main.forgot_email.*

class ForgotEmailActivity : BaseActivity(), View.OnClickListener {
    private var forgotTypeIntent: Int = 0
    lateinit var viewModel: ForgotViewModel
    lateinit var context: ForgotEmailActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_email)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            imgBack.rotation = 180F
        } else {
            imgBack.rotation = 0F
        }
        context = this
        forgotTypeIntent = intent.getIntExtra(ServerConstant.FORGOTTYPE_INTENT, 0)
        btnForgotemail.setOnClickListener(this@ForgotEmailActivity)
        imgBack.setOnClickListener(this@ForgotEmailActivity)

        apiForgotValidator()
    }

    private fun apiForgotValidator() {
        viewModel = ViewModelProviders.of(context).get(ForgotViewModel::class.java)
        viewModel.validationErr.observe(this, Observer { msg: Int ->
            hideProgressBarNew()
            showMessage(context, btnForgotemail, getString(msg))
        })
        viewModel.forgotResponse.observe(this, Observer { response: ForgotModelResponse ->
            hideProgressBarNew()
            if (response.success) {
                // showMessage(context,btnForgotemail,getString(R.string.sucess_login))
                if (response.code == 200) {
                    showMessage(context, btnForgotemail, response.message.toString())
                    launchActivityMain(LoginActivity::class.java)
                } else if (response.code == 204) {
                    showMessage(context, btnForgotemail, response.error_message.toString())
                }
            } else {
                showMessage(context, btnForgotemail, response.error_message.toString())
            }
        })
    }

    override fun onClick(v: View?) {
        if (v == imgBack) {
            onBackPressed()
        } else if (v == btnForgotemail) {
            showProgressBarNew()
            viewModel.forgotApiCall(
                edtForgotEmail.text.toString(),
                forgotTypeIntent.toString(),
                Utilities.isNetworkAvailable(context),
                appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString())
            )

        }

    }


    override fun onBackPressed() {
        finish()
    }

}