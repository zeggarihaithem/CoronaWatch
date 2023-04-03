package solutus.coronawatch.service

import android.webkit.WebView
import android.webkit.WebViewClient


internal open class Browser : WebViewClient() {
    override fun shouldOverrideUrlLoading(
        paramWebView: WebView,
        paramString: String?
    ): Boolean {
        paramWebView.loadUrl(paramString)
        return true
    }
}