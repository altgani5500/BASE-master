package com.parttime.enterprise.uicomman.forgots

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.parttime.enterprise.apiclients.ServerConstant
import com.parttime.enterprise.R
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import kotlinx.android.synthetic.main.activity_forgot.*

class ForgotActivity : BaseActivity(), View.OnClickListener {

    private var forgotType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            imgbck.rotation = 180F
        } else {
            imgbck.rotation = 0F
        }


        llForgotPass.setOnClickListener(this@ForgotActivity)
        llForgotEmail.setOnClickListener(this@ForgotActivity)
        imgbck.setOnClickListener(this@ForgotActivity)
    }

    override fun onClick(v: View?) {
        if (v == llForgotPass) {
            forgotType = 2
            forgotTypePass(forgotType)
        } else if (v == llForgotEmail) {
            forgotType = 1
            forgotTypePass(forgotType)
        } else if (v == imgbck) {
            onBackPressed()
        }


    }

    private fun forgotTypePass(forgotType: Int) {
        if (forgotType == 0) {
            showMessage(this@ForgotActivity, llForgotEmail, getString(R.string.forgotType_msg))
        } else {
            val intent = Intent(this, ForgotEmailActivity::class.java)
            intent.putExtra(ServerConstant.FORGOTTYPE_INTENT, forgotType)
            startActivity(intent)
            // finish()
        }

    }


    override fun onBackPressed() {
        finish()
    }
}