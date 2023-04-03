package solutus.coronawatch.service

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.webkit.WebChromeClient
import android.widget.FrameLayout


class ChromeClient(val activity: Activity) : WebChromeClient() {
    private var mCustomView: View? = null
    private var mCustomViewCallback: CustomViewCallback? = null
    private var mOriginalOrientation = 0
    private var mOriginalSystemUiVisibility = 0

    override fun getDefaultVideoPoster(): Bitmap? {
        return BitmapFactory.decodeResource(
            activity.applicationContext.resources,
            2130837573
        )
    }

    override fun onHideCustomView() {
        (activity.window.decorView as FrameLayout).removeView(mCustomView)
        mCustomView = null
        activity.window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
        activity.requestedOrientation = mOriginalOrientation
        mCustomViewCallback!!.onCustomViewHidden()
        mCustomViewCallback = null
    }

    override fun onShowCustomView(
        paramView: View?,
        paramCustomViewCallback: CustomViewCallback?
    ) {
        if (mCustomView != null) {
            onHideCustomView()
            return
        }
        mCustomView = paramView
        mOriginalSystemUiVisibility =
            activity.window.decorView.systemUiVisibility
        mOriginalOrientation = activity.requestedOrientation
        mCustomViewCallback = paramCustomViewCallback
        (activity.window.decorView as FrameLayout).addView(
            mCustomView,
            FrameLayout.LayoutParams(-1, -1)
        )
        activity.window.decorView.systemUiVisibility = 3846
    }
}