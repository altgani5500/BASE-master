package com.parttime.enterprise.uicomman

import android.os.Bundle
import android.os.Handler
import com.parttime.com.uicomman.auth.LoginActivity
import com.parttime.enterprise.R
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.homes.HomeActivity
import com.parttime.enterprise.uicomman.langs.SelectLanguageActivity

class SplashActivity : BaseActivity() {

    private val SPLASH_TIMER: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // nextScreen Launcher
        changeLocale()
        nextScreenOpen()
        if (appPrefence.getString(
                AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(),
                " "
            ).trim().isEmpty()
        ) {
            appPrefence.setString(AppPrefence.SharedPreferncesKeys.notificationONOFF.toString(), "1")
        }

    }

    private fun nextScreenOpen() {

        // check app prev login credential is available or not
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.apiToken.toString(), "").length > 2
            && appPrefence.getString(AppPrefence.SharedPreferncesKeys.email.toString(), "").length > 5
        ) {
            //Handler().postDelayed(Runnable {
                Handler().postDelayed(Runnable {
                    launchActivityMain(HomeActivity::class.java)
                    overridePendingTransition(R.anim.right_to_left_animation, R.anim.left_to_right_animation)
                }, SPLASH_TIMER)
            // }, SPLASH_TIMER)
        } else {
            Handler().postDelayed(Runnable {
                if (!appPrefence.getString(
                        AppPrefence.SharedPreferncesKeys.LANGUAGE_SCREEN.toString(),
                        "0"
                    )!!.contentEquals("1")
                ) {
                    launchActivityMain(SelectLanguageActivity::class.java)
                } else {
                    launchActivityMain(LoginActivity::class.java)
                }
                overridePendingTransition(R.anim.right_to_left_animation, R.anim.left_to_right_animation)
            }, SPLASH_TIMER)
        }

    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.right_to_left_animation, R.anim.left_to_right_animation)
    }

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.right_to_left_animation, R.anim.left_to_right_animation)
    }
}
