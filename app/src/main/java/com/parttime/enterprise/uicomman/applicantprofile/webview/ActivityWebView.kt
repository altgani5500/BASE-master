package com.parttime.enterprise.uicomman.applicantprofile.webview

import android.os.Bundle
import android.webkit.WebViewClient
import com.parttime.enterprise.R
import com.parttime.enterprise.prefences.AppPrefence
import com.parttime.enterprise.uicomman.BaseActivity
import kotlinx.android.synthetic.main.activity_webview.*


class ActivityWebView : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        if (appPrefence.getString(AppPrefence.SharedPreferncesKeys.LANGUAGE_KEY.toString(), "").equals("ar")) {
            accountwebViewback.rotation = 180F
        } else {
            accountwebViewback.rotation = 0F
        }
        accountwebViewback.setOnClickListener {
            onBackPressed()
        }
        openLink(intent.getStringExtra("LINK"))
    }


    private fun openLink(url: String) {
        url_view.webViewClient = WebViewClient()
        url_view.settings.setSupportZoom(true)
        url_view.settings.javaScriptEnabled = true
        //val url = "https://github.github.com/training-kit/downloads/github-git-cheat-sheet.pdf"
        url_view.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
    }


    override fun onBackPressed() {
        finish()
    }
}