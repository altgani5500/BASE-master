package com.parttime.enterprise.uicomman.langs

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.parttime.com.uicomman.auth.LoginActivity
import com.parttime.enterprise.R
import com.parttime.enterprise.helpers.Utilities
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.homes.HomeActivity
import kotlinx.android.synthetic.main.activity_change_language.*
import java.util.*


class ChangeLanguageActivity : BaseActivity(), View.OnClickListener {

    var language = ""
    var key = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_language)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            imgBack.rotation = 180F
        } else {
            imgBack.rotation = 0F
        }

        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(), "English").equals("English")) {
            rdbEnglish.isChecked = true
            rdbArabic.isChecked = false
        } else {
            rdbArabic.isChecked = true
            rdbEnglish.isChecked = false
        }
        applyclickListener()
    }

    private fun applyclickListener() {
        btnSubmit.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radioButtonID = radioGroup.checkedRadioButtonId
            val radioButton = radioGroup.findViewById<RadioButton>(radioButtonID)
            val index = radioGroup.indexOfChild(radioButton)
            // Logic
            when (index) {
                0 // first rdb button
                -> language = "English"

                1 // second rdb button
                -> language = "Arabic"

            }
        })
    }

    override fun onClick(v: View?) {
        if (v == btnSubmit) {
            if (language.equals("")) {
                Utilities.showToastLong(this, resources.getString(R.string.language))
            } else {
                if (language.equals("English")) {
                    key = "en"
                    setEnglish()
                } else if (language.equals("Arabic")) {
                    key = "ar"
                    setArabic()
                }


            }

        } else if (v == imgBack) {
            finish()
        }
    }

    private fun setArabic() {
        val locale = Locale("ar", "SA")
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLayoutDirection(config.locale)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            config.setLocale(locale)
            this.applicationContext.createConfigurationContext(config)
        }
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
        //TUGPrefs.putString("LAN_ARABIC", "SA")
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString(), "").isNullOrBlank()) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(), language)
            appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), key)
            startActivity(intent)
        } else {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(), language)
            appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), key)

            startActivity(intent)
        }

    }

    fun setEnglish() {
        val languageToLoad = "en_US"
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLayoutDirection(config.locale)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            config.setLocale(locale)
            this.applicationContext.createConfigurationContext(config)
        }
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
        // TUGPrefs.putString("LAN_ARABIC", "EN")

        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString(), "").isNullOrBlank()) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(), language)
            appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), key)
            startActivity(intent)
        } else {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(), language)
            appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), key)

            startActivity(intent)
        }

    }

}
