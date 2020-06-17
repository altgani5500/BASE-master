package com.parttime.enterprise.uicomman.langs

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import com.parttime.enterprise.R
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import com.parttime.enterprise.uicomman.walkthroughs.WalkThroughActivity
import kotlinx.android.synthetic.main.activity_language_selection.*
import java.util.*

class SelectLanguageActivity : BaseActivity(), View.OnClickListener {


    var language = ""
    var key = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)

        btnEnglishLang.setOnClickListener(this)
        btnArabicLang.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == btnEnglishLang) {
            key = "en"
            language = "English"
            setEnglish()
        } else if (v == btnArabicLang) {
            key = "ar"
            language = "Arabic"
            setArabic()
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
        //  if(appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString(),"").isNullOrBlank()){
        val intent = Intent(applicationContext, WalkThroughActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(), language)
        appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), key)
        appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_SCREEN.toString(), "1")
        startActivity(intent)
        //}/*else {
        /*  val intent = Intent(applicationContext, WalkThroughActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
          appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(), language)
          appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), key)
          appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_SCREEN.toString(),"1")
          startActivity(intent)
      }*/

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

        //  if(appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString(),"").isNullOrBlank()){
        val intent = Intent(applicationContext, WalkThroughActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(), language)
        appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), key)
        appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_SCREEN.toString(), "1")
        startActivity(intent)
        //   }else {
        /*  val intent = Intent(applicationContext, WalkThroughActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
          appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE.toString(), language)
          appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), key)
          appPrefence.setString(AppPrefence.SharedPreferncesKeys.LANGUAGE_SCREEN.toString(),"1")
          startActivity(intent)
      }*/

    }

}